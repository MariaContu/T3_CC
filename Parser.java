//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "exemploGC.y"
  import java.io.*;
  import java.util.ArrayList;
  import java.util.Stack;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short INT=258;
public final static short FLOAT=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short LIT=262;
public final static short VOID=263;
public final static short MAIN=264;
public final static short READ=265;
public final static short WRITE=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short FOR=269;
public final static short DO=270;
public final static short WHILE=271;
public final static short TRUE=272;
public final static short FALSE=273;
public final static short CONTINUE=274;
public final static short BREAK=275;
public final static short EQ=276;
public final static short LEQ=277;
public final static short GEQ=278;
public final static short NEQ=279;
public final static short AND=280;
public final static short OR=281;
public final static short ADDEQ=282;
public final static short INC=283;
public final static short DEC=284;
public final static short STRUCT=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    5,    0,    7,    9,    6,    4,    4,   10,   10,   10,
    2,    2,    2,    3,    1,    1,    1,    8,    8,   11,
   11,   11,   13,   11,   11,   14,   15,   11,   16,   11,
   18,   11,   21,   23,   24,   11,   11,   11,   25,   17,
   17,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   19,
   19,   20,   20,   22,   22,
};
final static short yylen[] = {                            2,
    0,    3,    0,    0,    9,    2,    0,    3,    5,    4,
    2,    1,    0,    3,    1,    1,    1,    2,    0,    2,
    3,    5,    0,    8,    5,    0,    0,    7,    0,    7,
    0,    8,    0,    0,    0,   12,    2,    2,    0,    3,
    0,    1,    3,    1,    1,    1,    3,    2,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    2,    2,    2,    5,    3,    5,    0,
    1,    0,    1,    0,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   15,   16,   17,    0,    0,    0,    0,    0,
    0,    0,    2,    6,    0,    0,    8,    0,   10,    0,
    0,   12,    0,    0,    9,   11,    3,   14,    0,   19,
    0,    0,   42,    0,    0,    0,    0,   31,   26,   44,
   45,    0,    0,    0,    0,    0,    0,   19,    0,   18,
    0,    0,   63,   64,    0,    0,    0,    0,    0,    0,
    0,    0,   38,   37,   65,   66,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   20,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,   21,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   33,    0,    0,    0,
    0,   25,   22,    0,    0,    0,    0,   27,    0,    0,
    0,    0,    0,    0,    0,    0,   39,   30,   34,    0,
   28,   24,    0,    0,   32,   40,    0,    0,   35,    0,
   36,
};
final static short yydgoto[] = {                          1,
    7,   21,   22,    8,    2,   13,   29,   31,   49,    9,
   50,   51,  115,   62,  135,  116,  138,   61,   93,  133,
  126,  148,  144,  150,  143,
};
final static short yysindex[] = {                         0,
    0, -255,    0,    0,    0, -256, -238, -241, -255, -121,
  -31, -229,    0,    0,  -10, -249,    0,    8,    0, -207,
 -113,    0,   10,   -6,    0,    0,    0,    0,  -67,    0,
    7,  -17,    0,   17,   18,   20,   21,    0,    0,    0,
    0,    5,    6, -190, -189,   26,   26,    0,  -56,    0,
  128,   26,    0,    0,   26, -187, -186, -188,   26,   26,
    7,   33,    0,    0,    0,    0,   12,  152,  -33,    0,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   26,    0,   26,  418,  418,   27,   45,   55,
  418,  418,   56, -157,   26,    0,    0,  -29,  -29,  -29,
  -29,  447,  440,  -29,  -29,  -11,  -11,   12,   12,   12,
  159,   26,   57,   58,   74,   78,    0,   80,  288,   26,
  418,    0,    0,   26,    7,   26,   26,    0,  418,  404,
 -147,  418,   63,  411,    7,   64,    0,    0,    0,   70,
    0,    0,    7,   26,    0,    0,  418,  100,    0,    7,
    0,
};
final static short yyrindex[] = {                         0,
    0, -120,    0,    0,    0,    0,    0,    0, -120,    0,
    0,    0,    0,    0,    0,   28,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   29,   35,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   92,
    0,    0,    0,    0,    0,    0,   66,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -20,  -16,   42,    0,  116,
  120,  108,    0,    0,    0,    0,    0,  321,  331,  353,
  362,  -35,  -26,  454,  467,  243,  295,   90,   97,  121,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -4,    0,    0,    0,    0,  110,    0,    0,    4,    0,
  -13,  113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  133,    0,    0,  135,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   25,    0,  156,  169,    0,    0,    0,  134,    0,    0,
  -44,  703,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=847;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   10,   16,    3,    4,    5,   61,   47,   83,    3,    4,
    5,   25,   81,   79,   60,   80,   94,   82,   11,   41,
   62,   12,   61,   61,   43,   83,   41,   17,   56,    6,
   81,   60,   60,   85,   18,   82,   69,   62,   62,   46,
   20,   43,   43,   55,   67,   20,   47,   23,   19,   24,
   27,   85,   28,   69,   69,   30,   57,   58,   46,   59,
   60,   67,   67,   63,   64,   47,   65,   66,   70,   88,
   89,   46,   95,   90,   85,   46,   46,   46,   68,   46,
  131,   46,   68,   68,   68,  113,   68,  112,   68,   48,
  141,   97,   46,   46,   46,  114,   46,   46,  146,   68,
   68,   68,   48,   68,   68,  151,   48,   48,   48,   41,
   48,   41,   48,  118,  117,  122,  123,  124,  125,  127,
  137,  139,  142,   48,   48,   48,   51,   48,  145,   48,
   51,   51,   51,   52,   51,   15,   51,   52,   52,   52,
  149,   52,    7,   52,    3,    4,    5,   51,   51,   51,
   70,   51,   13,    4,   52,   52,   52,   53,   52,   23,
   29,   53,   53,   53,   83,   53,   71,   53,   72,   81,
   79,   73,   80,   74,   82,   75,   26,   14,   53,   53,
   53,   69,   53,    0,    0,    0,   84,   78,   83,   77,
   85,    0,   96,   81,   79,   83,   80,    0,   82,    0,
   81,   79,    0,   80,    0,   82,    0,    0,    0,    0,
    0,   78,    0,   77,   85,    0,  120,    0,   78,    0,
   77,   85,    0,   32,    0,    0,    0,   33,    0,    0,
    0,   34,   35,   36,    0,   37,   38,   39,   40,   41,
   42,   43,    0,   41,   61,   61,    0,   41,    0,   44,
   45,   41,   41,   41,   60,   41,   41,   41,   41,   41,
   41,   41,    0,   32,   52,   53,   54,   33,    0,   41,
   41,   34,   35,   36,    0,   37,   38,   39,   40,   41,
   42,   43,   32,   49,    0,   49,   33,   49,    0,   44,
   45,    0,    0,    0,    0,    0,    0,   40,   41,    0,
   49,   49,   49,    0,   49,    0,    0,    0,   44,   45,
   46,   46,   46,   46,   46,   46,    0,   68,   68,   68,
   68,   68,   68,    0,   83,    0,    0,    0,  128,   81,
   79,    0,   80,    0,   82,   50,    0,   50,    0,   50,
    0,   48,   48,   48,   48,   48,   48,   78,    0,   77,
   85,    0,   50,   50,   50,    0,   50,    0,    0,    0,
    0,   56,    0,    0,    0,   51,   51,   51,   51,   51,
   51,   57,   52,   52,   52,   52,   52,   52,   56,   56,
   56,    0,   56,    0,    0,    0,    0,    0,   57,   57,
   57,    0,   57,   58,    0,    0,   53,   53,   53,   53,
   53,   53,   59,   71,   72,   73,   74,   75,   76,    0,
   58,   58,   58,    0,   58,    0,    0,    0,    0,   59,
   59,   59,    0,   59,    0,    0,    0,   71,   72,   73,
   74,   75,   76,    0,   71,   72,   73,   74,   75,   76,
   83,    0,    0,    0,  136,   81,   79,   83,   80,    0,
   82,  140,   81,   79,   83,   80,    0,   82,    0,   81,
   79,    0,   80,   78,   82,   77,   85,    0,    0,    0,
   78,    0,   77,   85,    0,    0,   83,   78,    0,   77,
   85,   81,   79,   83,   80,    0,   82,    0,   81,   79,
    0,   80,    0,   82,   54,    0,    0,    0,    0,   78,
    0,   77,   85,    0,    0,    0,   78,   55,   77,   85,
    0,   54,   54,   54,    0,   54,    0,    0,   49,   49,
   49,   49,   49,   49,   55,   55,   55,    0,   55,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   71,   72,   73,   74,   75,   76,    0,
   50,   50,   50,   50,   50,   50,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   56,   56,   56,   56,
   56,   56,    0,    0,    0,    0,   57,   57,   57,   57,
   57,   57,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,   58,
   58,   58,   58,   58,    0,    0,    0,   59,   59,   59,
   59,   59,   59,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   71,
   72,   73,   74,   75,   76,    0,   71,   72,   73,   74,
   75,   76,    0,   71,   72,   73,   74,   75,   76,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   71,   72,   73,   74,   75,
    0,    0,   71,   72,   73,   74,    0,    0,    0,   54,
   54,   54,   54,   54,   54,    0,    0,    0,    0,    0,
    0,    0,   55,   55,   55,   55,   55,   55,   67,   68,
    0,    0,    0,    0,   86,    0,    0,   87,    0,    0,
    0,   91,   92,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   98,   99,  100,  101,  102,  103,  104,
  105,  106,  107,  108,  109,  110,    0,  111,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  119,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  121,    0,    0,    0,    0,    0,
    0,    0,  129,    0,    0,    0,  130,    0,  132,  134,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  147,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
  257,  123,  258,  259,  260,   41,   40,   37,  258,  259,
  260,  125,   42,   43,   41,   45,   61,   47,  257,   33,
   41,  263,   58,   59,   41,   37,   40,   59,   46,  285,
   42,   58,   59,   63,  264,   47,   41,   58,   59,   33,
   16,   58,   59,   61,   41,   21,   40,   40,   59,  257,
   41,   63,   59,   58,   59,  123,   40,   40,   33,   40,
   40,   58,   59,   59,   59,   40,  257,  257,  125,  257,
  257,   37,   40,  262,   63,   41,   42,   43,   37,   45,
  125,   47,   41,   42,   43,   41,   45,   61,   47,  123,
  135,  125,   58,   59,   60,   41,   62,   63,  143,   58,
   59,   60,   37,   62,   63,  150,   41,   42,   43,  123,
   45,  125,   47,  271,   59,   59,   59,   44,   41,   40,
  268,   59,   59,   58,   59,   60,   37,   62,   59,  123,
   41,   42,   43,   37,   45,  257,   47,   41,   42,   43,
   41,   45,  263,   47,  258,  259,  260,   58,   59,   60,
   59,   62,  125,  125,   58,   59,   60,   37,   62,   44,
   41,   41,   42,   43,   37,   45,   59,   47,   59,   42,
   43,   59,   45,   41,   47,   41,   21,    9,   58,   59,
   60,   48,   62,   -1,   -1,   -1,   59,   60,   37,   62,
   63,   -1,   41,   42,   43,   37,   45,   -1,   47,   -1,
   42,   43,   -1,   45,   -1,   47,   -1,   -1,   -1,   -1,
   -1,   60,   -1,   62,   63,   -1,   58,   -1,   60,   -1,
   62,   63,   -1,  257,   -1,   -1,   -1,  261,   -1,   -1,
   -1,  265,  266,  267,   -1,  269,  270,  271,  272,  273,
  274,  275,   -1,  257,  280,  281,   -1,  261,   -1,  283,
  284,  265,  266,  267,  281,  269,  270,  271,  272,  273,
  274,  275,   -1,  257,  282,  283,  284,  261,   -1,  283,
  284,  265,  266,  267,   -1,  269,  270,  271,  272,  273,
  274,  275,  257,   41,   -1,   43,  261,   45,   -1,  283,
  284,   -1,   -1,   -1,   -1,   -1,   -1,  272,  273,   -1,
   58,   59,   60,   -1,   62,   -1,   -1,   -1,  283,  284,
  276,  277,  278,  279,  280,  281,   -1,  276,  277,  278,
  279,  280,  281,   -1,   37,   -1,   -1,   -1,   41,   42,
   43,   -1,   45,   -1,   47,   41,   -1,   43,   -1,   45,
   -1,  276,  277,  278,  279,  280,  281,   60,   -1,   62,
   63,   -1,   58,   59,   60,   -1,   62,   -1,   -1,   -1,
   -1,   41,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  281,   41,  276,  277,  278,  279,  280,  281,   58,   59,
   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   58,   59,
   60,   -1,   62,   41,   -1,   -1,  276,  277,  278,  279,
  280,  281,   41,  276,  277,  278,  279,  280,  281,   -1,
   58,   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   58,
   59,   60,   -1,   62,   -1,   -1,   -1,  276,  277,  278,
  279,  280,  281,   -1,  276,  277,  278,  279,  280,  281,
   37,   -1,   -1,   -1,   41,   42,   43,   37,   45,   -1,
   47,   41,   42,   43,   37,   45,   -1,   47,   -1,   42,
   43,   -1,   45,   60,   47,   62,   63,   -1,   -1,   -1,
   60,   -1,   62,   63,   -1,   -1,   37,   60,   -1,   62,
   63,   42,   43,   37,   45,   -1,   47,   -1,   42,   43,
   -1,   45,   -1,   47,   41,   -1,   -1,   -1,   -1,   60,
   -1,   62,   63,   -1,   -1,   -1,   60,   41,   62,   63,
   -1,   58,   59,   60,   -1,   62,   -1,   -1,  276,  277,
  278,  279,  280,  281,   58,   59,   60,   -1,   62,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  276,  277,  278,  279,  280,  281,   -1,
  276,  277,  278,  279,  280,  281,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
  280,  281,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,  280,  281,   -1,   -1,   -1,  276,  277,  278,
  279,  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,
  277,  278,  279,  280,  281,   -1,  276,  277,  278,  279,
  280,  281,   -1,  276,  277,  278,  279,  280,  281,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
   -1,   -1,  276,  277,  278,  279,   -1,   -1,   -1,  276,
  277,  278,  279,  280,  281,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  276,  277,  278,  279,  280,  281,   46,   47,
   -1,   -1,   -1,   -1,   52,   -1,   -1,   55,   -1,   -1,
   -1,   59,   60,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   71,   72,   73,   74,   75,   76,   77,
   78,   79,   80,   81,   82,   83,   -1,   85,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   95,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  112,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  120,   -1,   -1,   -1,  124,   -1,  126,  127,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  144,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=285;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'","'?'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"ID","INT","FLOAT","BOOL","NUM","LIT",
"VOID","MAIN","READ","WRITE","IF","ELSE","FOR","DO","WHILE","TRUE","FALSE",
"CONTINUE","BREAK","EQ","LEQ","GEQ","NEQ","AND","OR","ADDEQ","INC","DEC",
"STRUCT",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"prog : $$1 dList mainF",
"$$2 :",
"$$3 :",
"mainF : VOID MAIN '(' ')' $$2 '{' lcmd $$3 '}'",
"dList : decl dList",
"dList :",
"decl : type ID ';'",
"decl : STRUCT ID '{' field_list '}'",
"decl : STRUCT ID ID ';'",
"field_list : field_list field_decl",
"field_list : field_decl",
"field_list :",
"field_decl : type ID ';'",
"type : INT",
"type : FLOAT",
"type : BOOL",
"lcmd : lcmd cmd",
"lcmd :",
"cmd : exp ';'",
"cmd : '{' lcmd '}'",
"cmd : WRITE '(' LIT ')' ';'",
"$$4 :",
"cmd : WRITE '(' LIT $$4 ',' exp ')' ';'",
"cmd : READ '(' ID ')' ';'",
"$$5 :",
"$$6 :",
"cmd : WHILE $$5 '(' exp ')' $$6 cmd",
"$$7 :",
"cmd : IF '(' exp $$7 ')' cmd restoIf",
"$$8 :",
"cmd : DO $$8 cmd WHILE '(' exp ')' ';'",
"$$9 :",
"$$10 :",
"$$11 :",
"cmd : FOR '(' forInit ';' $$9 forCond ';' $$10 forInc ')' $$11 cmd",
"cmd : BREAK ';'",
"cmd : CONTINUE ';'",
"$$12 :",
"restoIf : ELSE $$12 cmd",
"restoIf :",
"exp : NUM",
"exp : ID '=' exp",
"exp : TRUE",
"exp : FALSE",
"exp : ID",
"exp : '(' exp ')'",
"exp : '!' exp",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '%' exp",
"exp : exp '>' exp",
"exp : exp '<' exp",
"exp : exp EQ exp",
"exp : exp LEQ exp",
"exp : exp GEQ exp",
"exp : exp NEQ exp",
"exp : exp OR exp",
"exp : exp AND exp",
"exp : ID ADDEQ exp",
"exp : ID INC",
"exp : ID DEC",
"exp : INC ID",
"exp : DEC ID",
"exp : exp '?' exp ':' exp",
"exp : ID '.' ID",
"exp : ID '.' ID '=' exp",
"forInit :",
"forInit : exp",
"forCond :",
"forCond : exp",
"forInc :",
"forInc : exp",
};

//#line 433 "exemploGC.y"

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
//#line 742 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 32 "exemploGC.y"
{ geraInicio(); }
break;
case 2:
//#line 32 "exemploGC.y"
{ geraAreaDados(); geraAreaLiterais(); }
break;
case 3:
//#line 34 "exemploGC.y"
{ System.out.println("_start:"); }
break;
case 4:
//#line 35 "exemploGC.y"
{ geraFinal(); }
break;
case 8:
//#line 42 "exemploGC.y"
{
          TS_entry nodo = ts.pesquisa(val_peek(1).sval);
          if (nodo != null)
              yyerror("(sem) variavel >" + val_peek(1).sval + "< jah declarada");
          else
              ts.insert(new TS_entry(val_peek(1).sval, val_peek(2).ival));   /* continua sendo TYPE_VAR internamente */
      }
break;
case 9:
//#line 50 "exemploGC.y"
{
      String structName = val_peek(3).sval;
      java.util.ArrayList campos = (java.util.ArrayList)val_peek(1).obj;
      TS_entry def = new TS_entry(structName, TS_entry.TYPE_STRUCT_DEF);
      def.setCampos(campos);
      ts.insert(def);
	  }
break;
case 10:
//#line 59 "exemploGC.y"
{
          String structType = val_peek(2).sval;
          String varName = val_peek(1).sval;
          TS_entry def = ts.pesquisa(structType);
          if (def == null || def.getCampos() == null) {
              yyerror("tipo struct nao declarado: " + structType);
          } else {
              TS_entry v = new TS_entry(varName, TS_entry.TYPE_STRUCT_VAR);
              v.setTipoStruct(structType);
              ts.insert(v);
          }
      }
break;
case 11:
//#line 75 "exemploGC.y"
{
        java.util.ArrayList campos = (java.util.ArrayList)val_peek(1).obj;
        if (campos == null) campos = new java.util.ArrayList();
        campos.add(val_peek(0).obj);
        yyval.obj = campos;
    }
break;
case 12:
//#line 82 "exemploGC.y"
{
        java.util.ArrayList campos = new java.util.ArrayList();
        campos.add(val_peek(0).obj);
        yyval.obj = campos;
    }
break;
case 13:
//#line 88 "exemploGC.y"
{
        yyval.obj = new java.util.ArrayList();
    }
break;
case 14:
//#line 95 "exemploGC.y"
{
        TS_entry campo = new TS_entry(val_peek(1).sval, val_peek(2).ival);
        yyval.obj = campo;
    }
break;
case 15:
//#line 102 "exemploGC.y"
{ yyval.ival = INT; }
break;
case 16:
//#line 103 "exemploGC.y"
{ yyval.ival = FLOAT; }
break;
case 17:
//#line 104 "exemploGC.y"
{ yyval.ival = BOOL; }
break;
case 20:
//#line 111 "exemploGC.y"
{  System.out.println("\t\t# terminou o bloco...");  }
break;
case 21:
//#line 112 "exemploGC.y"
{ System.out.println("\t\t# terminou o bloco..."); }
break;
case 22:
//#line 114 "exemploGC.y"
{ strTab.add(val_peek(2).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				System.out.println("\tCALL _writeln"); 
                                strCount++;
				}
break;
case 23:
//#line 123 "exemploGC.y"
{ strTab.add(val_peek(0).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				strCount++;
				}
break;
case 24:
//#line 131 "exemploGC.y"
{ 
			 System.out.println("\tPOPL %EAX"); 
			 System.out.println("\tCALL _write");	
			 System.out.println("\tCALL _writeln"); 
                        }
break;
case 25:
//#line 138 "exemploGC.y"
{
									System.out.println("\tPUSHL $_"+val_peek(2).sval);
									System.out.println("\tCALL _read");
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EAX, (%EDX)");
								}
break;
case 26:
//#line 145 "exemploGC.y"
{
			pRot.push(proxRot);  proxRot += 2;
            int inicio = pRot.peek();
            int fim    = inicio + 1;
            pContinue.push(inicio);
            pBreak.push(fim);
            System.out.printf("rot_%02d:\n", inicio);
		}
break;
case 27:
//#line 153 "exemploGC.y"
{
			System.out.println("\tPOPL %EAX   # desvia se falso...");
			System.out.println("\tCMPL $0, %EAX");
			System.out.printf("\tJE rot_%02d\n", (int)pRot.peek()+1);
		}
break;
case 28:
//#line 158 "exemploGC.y"
{
			System.out.printf("\tJMP rot_%02d   # volta pro inicio\n", (int)pContinue.peek());
            System.out.printf("rot_%02d:\n",(int)pBreak.peek());
            pRot.pop();
            pContinue.pop();
            pBreak.pop();
		}
break;
case 29:
//#line 166 "exemploGC.y"
{	
		pRot.push(proxRot);  proxRot += 2;
												 
		System.out.println("\tPOPL %EAX");
		System.out.println("\tCMPL $0, %EAX");
		System.out.printf("\tJE rot_%02d\n", pRot.peek());
		}
break;
case 30:
//#line 174 "exemploGC.y"
{
		System.out.printf("rot_%02d:\n",pRot.peek()+1);
		pRot.pop();
		}
break;
case 31:
//#line 179 "exemploGC.y"
{
		pRot.push(proxRot);  proxRot += 3;
        int corpo = pRot.peek();
        int cond  = corpo + 1;
        int fim   = corpo + 2;

        pBreak.push(fim);
        pContinue.push(cond);

        System.out.printf("rot_%02d:\n", corpo);
	}
break;
case 32:
//#line 190 "exemploGC.y"
{
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
break;
case 33:
//#line 207 "exemploGC.y"
{
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
	}
break;
case 34:
//#line 223 "exemploGC.y"
{
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
	}
break;
case 35:
//#line 236 "exemploGC.y"
{
		int size = pRot.size();
		int lEnd  = pRot.get(size-1);
		int lInc  = pRot.get(size-2);
		int lBody = pRot.get(size-3);
		int lCond = pRot.get(size-4);

		System.out.printf("\tJMP rot_%02d\n", lCond);

		System.out.printf("rot_%02d:\n", lBody);
	}
break;
case 36:
//#line 246 "exemploGC.y"
{
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
break;
case 37:
//#line 265 "exemploGC.y"
{
          if (pBreak.empty()) {
              yyerror("break fora de laco");
          } else {
              System.out.printf("\tJMP rot_%02d\n", (int)pBreak.peek());
          }
      }
break;
case 38:
//#line 272 "exemploGC.y"
{
          if (pContinue.empty()) {
              yyerror("continue fora de laco");
          } else {
              System.out.printf("\tJMP rot_%02d\n", (int)pContinue.peek());
          }
      }
break;
case 39:
//#line 282 "exemploGC.y"
{
											System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
											System.out.printf("rot_%02d:\n",pRot.peek());
								}
break;
case 41:
//#line 286 "exemploGC.y"
{
		    System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
				System.out.printf("rot_%02d:\n",pRot.peek());
				}
break;
case 42:
//#line 292 "exemploGC.y"
{ System.out.println("\tPUSHL $"+val_peek(0).sval); }
break;
case 43:
//#line 293 "exemploGC.y"
{
		System.out.println("\tPOPL %EDX");
		System.out.println("\tMOVL %EDX, _"+val_peek(2).sval);
		System.out.println("\tPUSHL %EDX");
	}
break;
case 44:
//#line 298 "exemploGC.y"
{ System.out.println("\tPUSHL $1"); }
break;
case 45:
//#line 299 "exemploGC.y"
{ System.out.println("\tPUSHL $0"); }
break;
case 46:
//#line 300 "exemploGC.y"
{ System.out.println("\tPUSHL _"+val_peek(0).sval); }
break;
case 48:
//#line 302 "exemploGC.y"
{ gcExpNot(); }
break;
case 49:
//#line 304 "exemploGC.y"
{ gcExpArit('+'); }
break;
case 50:
//#line 305 "exemploGC.y"
{ gcExpArit('-'); }
break;
case 51:
//#line 306 "exemploGC.y"
{ gcExpArit('*'); }
break;
case 52:
//#line 307 "exemploGC.y"
{ gcExpArit('/'); }
break;
case 53:
//#line 308 "exemploGC.y"
{ gcExpArit('%'); }
break;
case 54:
//#line 310 "exemploGC.y"
{ gcExpRel('>'); }
break;
case 55:
//#line 311 "exemploGC.y"
{ gcExpRel('<'); }
break;
case 56:
//#line 312 "exemploGC.y"
{ gcExpRel(EQ); }
break;
case 57:
//#line 313 "exemploGC.y"
{ gcExpRel(LEQ); }
break;
case 58:
//#line 314 "exemploGC.y"
{ gcExpRel(GEQ); }
break;
case 59:
//#line 315 "exemploGC.y"
{ gcExpRel(NEQ); }
break;
case 60:
//#line 317 "exemploGC.y"
{ gcExpLog(OR); }
break;
case 61:
//#line 318 "exemploGC.y"
{ gcExpLog(AND); }
break;
case 62:
//#line 320 "exemploGC.y"
{
		System.out.println("\tPOPL %EAX");
		System.out.println("\tMOVL _"+val_peek(2).sval+", %EDX");
		System.out.println("\tADDL %EAX, %EDX");
		System.out.println("\tMOVL %EDX, _"+val_peek(2).sval);
		System.out.println("\tPUSHL %EDX");
	}
break;
case 63:
//#line 328 "exemploGC.y"
{
       System.out.println("\tPUSHL _"+val_peek(1).sval+"");
       System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
       System.out.println("\tADDL $1, %EAX");
       System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
    }
break;
case 64:
//#line 335 "exemploGC.y"
{
        System.out.println("\tPUSHL _"+val_peek(1).sval+"");
        System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
        System.out.println("\tSUBL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
    }
break;
case 65:
//#line 342 "exemploGC.y"
{
        System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
        System.out.println("\tADDL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
        System.out.println("\tPUSHL %EAX");
    }
break;
case 66:
//#line 349 "exemploGC.y"
{
        System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
        System.out.println("\tSUBL $1, %EAX");
        System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
        System.out.println("\tPUSHL %EAX");
    }
break;
case 67:
//#line 356 "exemploGC.y"
{
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
break;
case 68:
//#line 378 "exemploGC.y"
{
        TS_entry var = ts.pesquisa(val_peek(2).sval);
        if (var == null) {
            yyerror("variavel nao declarada: " + val_peek(2).sval);
            System.out.println("\tPUSHL $0");
        } else if (var.getTipoStruct() == null) {
            yyerror("nao e struct: " + val_peek(2).sval);
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
                    if (c.getId().equals(val_peek(0).sval)) { campo = c; break; }
                }
                if (campo == null) {
                    yyerror("campo nao existe: " + val_peek(0).sval);
                    System.out.println("\tPUSHL $0");
                } else {
                    System.out.println("\tPUSHL _" + val_peek(2).sval + "_" + val_peek(0).sval);
                }
            }
        }
    }
break;
case 69:
//#line 409 "exemploGC.y"
{
        System.out.println("\tPOPL %EAX");
        System.out.println("\tMOVL %EAX, _" + val_peek(4).sval + "_" + val_peek(2).sval);
        System.out.println("\tPUSHL %EAX");
    }
break;
case 71:
//#line 419 "exemploGC.y"
{
		System.out.println("\tPOPL %EAX");
	}
break;
case 72:
//#line 424 "exemploGC.y"
{System.out.println("\tPUSHL $1");}
break;
case 75:
//#line 429 "exemploGC.y"
{
		System.out.println("\tPOPL %EAX");
	}
break;
//#line 1419 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
