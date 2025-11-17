import java.util.ArrayList;
import java.util.Iterator;

public class TabSimb
{
    private ArrayList<TS_entry> lista;
    
    public TabSimb( )
    {
        lista = new ArrayList<TS_entry>();
    }
    
    public void insert( TS_entry nodo ) {
      lista.add(nodo);
    }    
    
    public void listar() {
      System.out.println("\n\n# Listagem da tabela de simbolos:\n");
      for (TS_entry nodo : lista) {
          System.out.println("# " + nodo);
          Object camposObj = nodo.getCampos();
          if (camposObj != null && camposObj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<TS_entry> campos = (ArrayList<TS_entry>) camposObj;
              if (!campos.isEmpty()) {
                  System.out.println("#    campos:");
                  for (TS_entry f : campos) {
                      System.out.println("#      - " + f.getId() + " : tipo " + f.getTipo());
                  }
              }
          } else if (nodo.getTipoStruct() != null) {
              System.out.println("#    variavel struct do tipo: " + nodo.getTipoStruct());
          }
      }
    }
      
    public TS_entry pesquisa(String umId) {
      for (TS_entry nodo : lista) {
          if (nodo.getId().equals(umId)) {
	      return nodo;
          }
      }
      return null;
    }
    
	public void geraGlobais() {
	    for (TS_entry nodo : lista) {
	        Object camposObj = nodo.getCampos();
	        if (camposObj != null && camposObj instanceof ArrayList) {
	            continue;
	        }
	        if (nodo.getTipoStruct() != null) {
	            String tipoStruct = nodo.getTipoStruct();
	            TS_entry def = pesquisa(tipoStruct);
	            if (def != null) {
	                Object defCamposObj = def.getCampos();
	                if (defCamposObj != null && defCamposObj instanceof ArrayList) {
	                    @SuppressWarnings("unchecked")
	                    ArrayList<TS_entry> campos = (ArrayList<TS_entry>) defCamposObj;
	                    System.out.println("_" + nodo.getId() + ":" + "	.zero 4");
	                    for (TS_entry f : campos) {
	                        System.out.println("_" + nodo.getId() + "_" + f.getId() + ":" + "	.zero 4");
	                    }
	                    continue;
	                }
	            }
	            System.out.println("_" + nodo.getId() + ":" + "	.zero 4");
	            continue;
	        }
        	System.out.println("_"+nodo.getId()+":"+"	.zero 4");
	    }
	}
}
