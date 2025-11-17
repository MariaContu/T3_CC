%{
  import java.io.*;
  import java.util.ArrayList;
  import java.util.Stack;
%}

%token ID, INT, FLOAT, BOOL, NUM, LIT, VOID, MAIN, READ, WRITE, IF, ELSE
%token FOR, DO, WHILE, TRUE, FALSE, IF, ELSE, CONTINUE, BREAK
%token EQ, LEQ, GEQ, NEQ 
%token AND, OR
%token ADDEQ
%token INC, DEC
%token STRUCT

%right '=' ADDEQ
%left OR
%left AND
%left  '>' '<' EQ LEQ GEQ NEQ
%left '+' '-'
%left '*' '/' '%'
%left '!' 
%right INC DEC

%type <sval> ID
%type <sval> LIT
%type <sval> NUM
%type <ival> type
%type <obj>  field_list field_decl

%%

prog : { geraInicio(); } dList mainF { geraAreaDados(); geraAreaLiterais(); } ;

mainF : VOID MAIN '(' ')'   { System.out.println("_start:"); }
        '{' lcmd  { geraFinal(); } '}'
         ; 

dList : decl dList | ;

decl :
    /* variavel simples */ 
      type ID ';' {
          TS_entry nodo = ts.pesquisa($2);
          if (nodo != null)
              yyerror("(sem) variavel >" + $2 + "< jah declarada");
          else
              ts.insert(new TS_entry($2, $1));   /* continua sendo TYPE_VAR internamente */
      }
    /* definicao de struct: struct Nome { campos } */
    | STRUCT ID '{' field_list '}' {
      String structName = $2;
      java.util.ArrayList campos = (java.util.ArrayList)$4;
      TS_entry def = new TS_entry(structName, TS_entry.TYPE_STRUCT_DEF);
      def.setCampos(campos);
      ts.insert(def);
	  }

    /* declaracao de variavel do tipo struct: struct Nome var; */
    | STRUCT ID ID ';' {
          String structType = $2;
          String varName = $3;
          TS_entry def = ts.pesquisa(structType);
          if (def == null || def.getCampos() == null) {
              yyerror("tipo struct nao declarado: " + structType);
          } else {
              TS_entry v = new TS_entry(varName, TS_entry.TYPE_STRUCT_VAR);
              v.setTipoStruct(structType);
              ts.insert(v);
          }
      }
    ;

field_list :
    field_list field_decl
    {
        java.util.ArrayList campos = (java.util.ArrayList)$1;
        if (campos == null) campos = new java.util.ArrayList();
        campos.add($2);
        $$ = campos;
    }
  | field_decl
    {
        java.util.ArrayList campos = new java.util.ArrayList();
        campos.add($1);
        $$ = campos;
    }
  | /* vazio */
    {
        $$ = new java.util.ArrayList();
    }
;

field_decl :
    type ID ';'
    {
        TS_entry campo = new TS_entry($2, $1);
        $$ = campo;
    }
;


type : INT    { $$ = INT; }
     | FLOAT  { $$ = FLOAT; }
     | BOOL   { $$ = BOOL; }
     ;

lcmd : lcmd cmd
	   |
	   ;
	   
cmd :  exp ';' {  System.out.println("\t\t# terminou o bloco...");  }
			| '{' lcmd '}' { System.out.println("\t\t# terminou o bloco..."); }

      | WRITE '(' LIT ')' ';' { strTab.add($3);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				System.out.println("\tCALL _writeln"); 
                                strCount++;
				}
      
	  | WRITE '(' LIT 
                              { strTab.add($3);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				strCount++;
				}

                    ',' exp ')' ';' 
			{ 
			 System.out.println("\tPOPL %EAX"); 
			 System.out.println("\tCALL _write");	
			 System.out.println("\tCALL _writeln"); 
                        }
         
     | READ '(' ID ')' ';'								
								{
									System.out.println("\tPUSHL $_"+$3);
									System.out.println("\tCALL _read");
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EAX, (%EDX)");
								}
         
    | WHILE {
			pRot.push(proxRot);  proxRot += 2;
            int inicio = pRot.peek();
            int fim    = inicio + 1;
            pContinue.push(inicio);
            pBreak.push(fim);
            System.out.printf("rot_%02d:\n", inicio);
		} 
		'(' exp ')' {
			System.out.println("\tPOPL %EAX   # desvia se falso...");
			System.out.println("\tCMPL $0, %EAX");
			System.out.printf("\tJE rot_%02d\n", (int)pRot.peek()+1);
		} 
		cmd		{
			System.out.printf("\tJMP rot_%02d   # volta pro inicio\n", (int)pContinue.peek());
            System.out.printf("rot_%02d:\n",(int)pBreak.peek());
            pRot.pop();
            pContinue.pop();
            pBreak.pop();
		}  
							
	| IF '(' exp {	
		pRot.push(proxRot);  proxRot += 2;
												 
		System.out.println("\tPOPL %EAX");
		System.out.println("\tCMPL $0, %EAX");
		System.out.printf("\tJE rot_%02d\n", pRot.peek());
		}
	')' cmd 
    restoIf {
		System.out.printf("rot_%02d:\n",pRot.peek()+1);
		pRot.pop();
		}

	| DO {
		pRot.push(proxRot);  proxRot += 3;
        int corpo = pRot.peek();
        int cond  = corpo + 1;
        int fim   = corpo + 2;

        pBreak.push(fim);
        pContinue.push(cond);

        System.out.printf("rot_%02d:\n", corpo);
	}
	cmd WHILE '(' exp ')' ';' {
		int corpo = pRot.peek();
        int cond  = corpo + 1;
        int fim   = corpo + 2;

        System.out.printf("rot_%02d:\n", cond);
		System.out.println("\tPOPL %EAX");
        System.out.println("\tCMPL $0, %EAX");
        System.out.printf("\tJNE rot_%02d\n", corpo);

        System.out.printf("rot_%02d:\n", fim);

        pRot.pop();
        pBreak.pop();
        pContinue.pop();
	}
	
	| FOR '(' forInit ';' {
		int lCond = proxRot++;
        int lBody = proxRot++;
        int lInc  = proxRot++;
        int lEnd  = proxRot++;

		pRot.push(lCond);
        pRot.push(lBody);
        pRot.push(lInc);
        pRot.push(lEnd); 

		pBreak.push(lEnd);
		pContinue.push(lInc);
	
		System.out.printf("\tJMP rot_%02d\n", lCond);
		System.out.printf("rot_%02d:\n", lCond);
	} forCond ';' {
		int size = pRot.size();
		int lEnd  = pRot.get(size-1);
		int lInc  = pRot.get(size-2);
		int lBody = pRot.get(size-3);
		int lCond = pRot.get(size-4);

		System.out.println("\tPOPL %EAX   # condicao do for");
		System.out.println("\tCMPL $0, %EAX");
		System.out.printf("\tJE rot_%02d\n", lEnd);
		System.out.printf("\tJMP rot_%02d\n", lBody);

		System.out.printf("rot_%02d:\n", lInc);
	} forInc ')' {
		int size = pRot.size();
		int lEnd  = pRot.get(size-1);
		int lInc  = pRot.get(size-2);
		int lBody = pRot.get(size-3);
		int lCond = pRot.get(size-4);

		System.out.printf("\tJMP rot_%02d\n", lCond);

		System.out.printf("rot_%02d:\n", lBody);
	} cmd {
		int size = pRot.size();
		int lEnd  = pRot.get(size-1);
		int lInc  = pRot.get(size-2);
		int lBody = pRot.get(size-3);
		int lCond = pRot.get(size-4);

		System.out.printf("\tJMP rot_%02d\n", lInc);

		System.out.printf("rot_%02d:\n", lEnd);

		pRot.pop();
		pRot.pop();
		pRot.pop();
		pRot.pop();
		
		pBreak.pop();
        pContinue.pop();
	}
	| BREAK ';' {
          if (pBreak.empty()) {
              yyerror("break fora de laco");
          } else {
              System.out.printf("\tJMP rot_%02d\n", (int)pBreak.peek());
          }
      }
    | CONTINUE ';' {
          if (pContinue.empty()) {
              yyerror("continue fora de laco");
          } else {
              System.out.printf("\tJMP rot_%02d\n", (int)pContinue.peek());
          }
      }

    ;
     
restoIf : ELSE  {
											System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
											System.out.printf("rot_%02d:\n",pRot.peek());
								} cmd  
		| {
		    System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
				System.out.printf("rot_%02d:\n",pRot.peek());
				} 
		;										

exp :  NUM  { System.out.println("\tPUSHL $"+$1); } 
	|  ID '=' exp {
		System.out.println("\tPOPL %EDX");
		System.out.println("\tMOVL %EDX, _"+$1);
		System.out.println("\tPUSHL %EDX");
	}
    |  TRUE  { System.out.println("\tPUSHL $1"); } 
    |  FALSE  { System.out.println("\tPUSHL $0"); }      
 	| ID   { System.out.println("\tPUSHL _"+$1); }
    | '(' exp	')' 
    | '!' exp       { gcExpNot(); }
     
	| exp '+' exp		{ gcExpArit('+'); }
	| exp '-' exp		{ gcExpArit('-'); }
	| exp '*' exp		{ gcExpArit('*'); }
	| exp '/' exp		{ gcExpArit('/'); }
	| exp '%' exp		{ gcExpArit('%'); }
																		
	| exp '>' exp		{ gcExpRel('>'); }
	| exp '<' exp		{ gcExpRel('<'); }											 
	| exp EQ exp		{ gcExpRel(EQ); }											 
	| exp LEQ exp		{ gcExpRel(LEQ); }											 
	| exp GEQ exp		{ gcExpRel(GEQ); }											 
	| exp NEQ exp		{ gcExpRel(NEQ); }											 
												 
	| exp OR exp		{ gcExpLog(OR); }											 
	| exp AND exp		{ gcExpLog(AND); }											

	| ID ADDEQ exp {
		System.out.println("\tPOPL %EAX");
		System.out.println("\tMOVL _"+$1+", %EDX");
		System.out.println("\tADDL %EAX, %EDX");
		System.out.println("\tMOVL %EDX, _"+$1);
		System.out.println("\tPUSHL %EDX");
	}	

	| ID INC {
       System.out.println("\tPUSHL _"+$1+"");
       System.out.println("\tMOVL _"+$1+", %EAX");
       System.out.println("\tADDL $1, %EAX");
       System.out.println("\tMOVL %EAX, _"+$1);
    }

    | ID DEC {
        System.out.println("\tPUSHL _"+$1+"");
        System.out.println("\tMOVL _"+$1+", %EAX");
        System.out.println("\tSUBL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+$1);
    }

    | INC ID {
        System.out.println("\tMOVL _"+$2+", %EAX");
        System.out.println("\tADDL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+$2);
        System.out.println("\tPUSHL %EAX");
    }

    | DEC ID {
        System.out.println("\tMOVL _"+$2+", %EAX");
        System.out.println("\tSUBL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+$2);
        System.out.println("\tPUSHL %EAX");
    }

	| exp '?' exp ':' exp {
		System.out.println("\tPOPL %ECX");
        System.out.println("\tPOPL %EBX");
        System.out.println("\tPOPL %EAX");

		int rFalse = proxRot++;
        int rFim   = proxRot++;

        System.out.println("\tCMPL $0, %EAX");
        System.out.printf("\tJE rot_%02d\n", rFalse);

        System.out.println("\tMOVL %EBX, %EDX");
        System.out.printf("\tJMP rot_%02d\n", rFim);

        System.out.printf("rot_%02d:\n", rFalse);
        System.out.println("\tMOVL %ECX, %EDX");

        System.out.printf("rot_%02d:\n", rFim);
        System.out.println("\tPUSHL %EDX");
	}

    /* acesso simples a campo: var.campo */
    | ID '.' ID {
        TS_entry var = ts.pesquisa($1);
        if (var == null) {
            yyerror("variavel nao declarada: " + $1);
            System.out.println("\tPUSHL $0");
        } else if (var.getTipoStruct() == null) {
            yyerror("nao e struct: " + $1);
            System.out.println("\tPUSHL $0");
        } else {
            TS_entry def = ts.pesquisa(var.getTipoStruct());
            if (def == null || def.getCampos() == null) {
                yyerror("definicao da struct nao encontrada: " + var.getTipoStruct());
                System.out.println("\tPUSHL $0");
            } else {
                java.util.ArrayList campos = def.getCampos();
                TS_entry campo = null;
                for (int i=0;i<campos.size();i++) {
                    TS_entry c = (TS_entry)campos.get(i);
                    if (c.getId().equals($3)) { campo = c; break; }
                }
                if (campo == null) {
                    yyerror("campo nao existe: " + $3);
                    System.out.println("\tPUSHL $0");
                } else {
                    System.out.println("\tPUSHL _" + $1 + "_" + $3);
                }
            }
        }
    }

    /* atribuicao em campo: var.campo = exp */
    | ID '.' ID '=' exp {
        System.out.println("\tPOPL %EAX");
        System.out.println("\tMOVL %EAX, _" + $1 + "_" + $3);
        System.out.println("\tPUSHL %EAX");
    }

	;
							

forInit : 
	| exp {
		System.out.println("\tPOPL %EAX");
	}
	;

forCond : {System.out.println("\tPUSHL $1");}
	| exp
	;

forInc : 
	| exp {
		System.out.println("\tPOPL %EAX");
	}
%%

  private Yylex lexer;

  private TabSimb ts = new TabSimb();

  private int strCount = 0;
  private ArrayList<String> strTab = new ArrayList<String>();

  private Stack<Integer> pRot = new Stack<Integer>();
  private int proxRot = 1;

  private Stack<Integer> pBreak = new Stack<Integer>();
  private Stack<Integer> pContinue = new Stack<Integer>();

  public static int ARRAY = 100;


  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error + "  linha: " + lexer.getLine());
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }  

  public void setDebug(boolean debug) {
    yydebug = debug;
  }

  public void listarTS() { ts.listar();}

  public static void main(String args[]) throws IOException {

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
      yyparser.yyparse();
      // yyparser.listarTS();

    }
    else {
      // interactive mode
      System.out.println("\n\tFormato: java Parser entrada.cmm >entrada.s\n");
    }

  }

							
		void gcExpArit(int oparit) {
 				System.out.println("\tPOPL %EBX");
   			System.out.println("\tPOPL %EAX");

   		switch (oparit) {
     		case '+' : System.out.println("\tADDL %EBX, %EAX" ); break;
     		case '-' : System.out.println("\tSUBL %EBX, %EAX" ); break;
     		case '*' : System.out.println("\tIMULL %EBX, %EAX" ); break;

    		case '/': 
           		     System.out.println("\tMOVL $0, %EDX");
           		     System.out.println("\tIDIVL %EBX");
           		     break;
     		case '%': 
           		     System.out.println("\tMOVL $0, %EDX");
           		     System.out.println("\tIDIVL %EBX");
           		     System.out.println("\tMOVL %EDX, %EAX");
           		     break;
    		}
   		System.out.println("\tPUSHL %EAX");
		}

	public void gcExpRel(int oprel) {

    System.out.println("\tPOPL %EAX");
    System.out.println("\tPOPL %EDX");
    System.out.println("\tCMPL %EAX, %EDX");
    System.out.println("\tMOVL $0, %EAX");
    
    switch (oprel) {
       case '<':  			System.out.println("\tSETL  %AL"); break;
       case '>':  			System.out.println("\tSETG  %AL"); break;
       case Parser.EQ:  System.out.println("\tSETE  %AL"); break;
       case Parser.GEQ: System.out.println("\tSETGE %AL"); break;
       case Parser.LEQ: System.out.println("\tSETLE %AL"); break;
       case Parser.NEQ: System.out.println("\tSETNE %AL"); break;
       }
    
    System.out.println("\tPUSHL %EAX");

	}


	public void gcExpLog(int oplog) {

	   	System.out.println("\tPOPL %EDX");
 		 	System.out.println("\tPOPL %EAX");

  	 	System.out.println("\tCMPL $0, %EAX");
 		  System.out.println("\tMOVL $0, %EAX");
   		System.out.println("\tSETNE %AL");
   		System.out.println("\tCMPL $0, %EDX");
   		System.out.println("\tMOVL $0, %EDX");
   		System.out.println("\tSETNE %DL");

   		switch (oplog) {
    			case Parser.OR:  System.out.println("\tORL  %EDX, %EAX");  break;
    			case Parser.AND: System.out.println("\tANDL  %EDX, %EAX"); break;
       }

    	System.out.println("\tPUSHL %EAX");
	}

	public void gcExpNot(){

  	 System.out.println("\tPOPL %EAX" );
 	   System.out.println("	\tNEGL %EAX" );
  	 System.out.println("	\tPUSHL %EAX");
	}

   private void geraInicio() {
			System.out.println(".text\n\n#\t nome COMPLETO e matricula dos componentes do grupo...\n#\n"); 
			System.out.println(".GLOBL _start\n\n");  
   }

   private void geraFinal(){
	
			System.out.println("\n\n");
			System.out.println("#");
			System.out.println("# devolve o controle para o SO (final da main)");
			System.out.println("#");
			System.out.println("\tmov $0, %ebx");
			System.out.println("\tmov $1, %eax");
			System.out.println("\tint $0x80");
	
			System.out.println("\n");
			System.out.println("#");
			System.out.println("# Funcoes da biblioteca (IO)");
			System.out.println("#");
			System.out.println("\n");
			System.out.println("_writeln:");
			System.out.println("\tMOVL $__fim_msg, %ECX");
			System.out.println("\tDECL %ECX");
			System.out.println("\tMOVB $10, (%ECX)");
			System.out.println("\tMOVL $1, %EDX");
			System.out.println("\tJMP _writeLit");
			System.out.println("_write:");
			System.out.println("\tMOVL $__fim_msg, %ECX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tCMPL $0, %EAX");
			System.out.println("\tJGE _write3");
			System.out.println("\tNEGL %EAX");
			System.out.println("\tMOVL $1, %EBX");
			System.out.println("_write3:");
			System.out.println("\tPUSHL %EBX");
			System.out.println("\tMOVL $10, %EBX");
			System.out.println("_divide:");
			System.out.println("\tMOVL $0, %EDX");
			System.out.println("\tIDIVL %EBX");
			System.out.println("\tDECL %ECX");
			System.out.println("\tADD $48, %DL");
			System.out.println("\tMOVB %DL, (%ECX)");
			System.out.println("\tCMPL $0, %EAX");
			System.out.println("\tJNE _divide");
			System.out.println("\tPOPL %EBX");
			System.out.println("\tCMPL $0, %EBX");
			System.out.println("\tJE _print");
			System.out.println("\tDECL %ECX");
			System.out.println("\tMOVB $'-', (%ECX)");
			System.out.println("_print:");
			System.out.println("\tMOVL $__fim_msg, %EDX");
			System.out.println("\tSUBL %ECX, %EDX");
			System.out.println("_writeLit:");
			System.out.println("\tMOVL $1, %EBX");
			System.out.println("\tMOVL $4, %EAX");
			System.out.println("\tint $0x80");
			System.out.println("\tRET");
			System.out.println("_read:");
			System.out.println("\tMOVL $15, %EDX");
			System.out.println("\tMOVL $__msg, %ECX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tMOVL $3, %EAX");
			System.out.println("\tint $0x80");
			System.out.println("\tMOVL $0, %EAX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tMOVL $0, %EDX");
			System.out.println("\tMOVL $__msg, %ECX");
			System.out.println("\tCMPB $'-', (%ECX)");
			System.out.println("\tJNE _reading");
			System.out.println("\tINCL %ECX");
			System.out.println("\tINC %BL");
			System.out.println("_reading:");
			System.out.println("\tMOVB (%ECX), %DL");
			System.out.println("\tCMP $10, %DL");
			System.out.println("\tJE _fimread");
			System.out.println("\tSUB $48, %DL");
			System.out.println("\tIMULL $10, %EAX");
			System.out.println("\tADDL %EDX, %EAX");
			System.out.println("\tINCL %ECX");
			System.out.println("\tJMP _reading");
			System.out.println("_fimread:");
			System.out.println("\tCMPB $1, %BL");
			System.out.println("\tJNE _fimread2");
			System.out.println("\tNEGL %EAX");
			System.out.println("_fimread2:");
			System.out.println("\tRET");
			System.out.println("\n");
     }

     private void geraAreaDados(){
			System.out.println("");		
			System.out.println("#");
			System.out.println("# area de dados");
			System.out.println("#");
			System.out.println(".data");
			System.out.println("#");
			System.out.println("# variaveis globais");
			System.out.println("#");
			ts.geraGlobais();	
			System.out.println("");
	
    }

     private void geraAreaLiterais() { 

         System.out.println("#\n# area de literais\n#");
         System.out.println("__msg:");
	       System.out.println("\t.zero 30");
	       System.out.println("__fim_msg:");
	       System.out.println("\t.byte 0");
	       System.out.println("\n");

         for (int i = 0; i<strTab.size(); i++ ) {
             System.out.println("_str_"+i+":");
             System.out.println("\t .ascii \""+strTab.get(i)+"\""); 
	           System.out.println("_str_"+i+"Len = . - _str_"+i);  
	      }		
   }
