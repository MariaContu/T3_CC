import java.util.ArrayList;

public class TS_entry {
   public static final int TYPE_VAR = 1;         // variável simples
   public static final int TYPE_STRUCT_DEF = 2;  // definição de struct (tipo)
   public static final int TYPE_STRUCT_VAR = 3;  // variável do tipo struct

   private String id;
   private int tipo;
   private int nElem;
   private int tipoBase;

   private ArrayList<TS_entry> campos;  // se for struct (definição)
   private String tipoStruct;           // se variável for struct (nome do tipo)

   public TS_entry(String umId, int umTipo, int ne, int umTBase) {
      id = umId;
      tipo = umTipo;
      nElem = ne;
      tipoBase = umTBase;
      campos = null;
      tipoStruct = null;
   }

   // construtor simples
   public TS_entry(String umId, int umTipo) {
      this(umId, umTipo, -1, -1);
   }

   // construtor para campo (nome + tipo base)
   public TS_entry(String umId, int umTipoBase, boolean isField) {
      this(umId, TYPE_VAR, -1, umTipoBase);
   }

   public String getId() { return id; }
   public int getTipo() { return tipo; }
   public int getNumElem() { return nElem; }
   public int getTipoBase() { return tipoBase; }

   // métodos para campos como ArrayList (compatível com .y e TabSimb)
   public void setCampos(ArrayList<TS_entry> c) {
      this.campos = c;
   }

   public ArrayList<TS_entry> getCampos() {
      return campos;
   }

   public void setTipoStruct(String t) {
      this.tipoStruct = t;
   }

   public String getTipoStruct() {
      return tipoStruct;
   }

   // opcional: permitir alterar tipo depois da criação
   public void setTipo(int t) {
      this.tipo = t;
   }

   @Override
   public String toString() {
       String s = "Id: " + id + "\t tipo: " + tipo;
       if (tipo == TYPE_STRUCT_DEF && campos != null) {
           s += "\t{";
           for (TS_entry f : campos) s += " " + f.getId();
           s += " }";
       } else if (tipo == TYPE_STRUCT_VAR && tipoStruct != null) {
           s += "\tstructTipo: " + tipoStruct;
       } else if (nElem != -1) {
           s += "\t array(" + nElem + "): " + tipoBase;
       }
       return s;
   }
}
