import java.io.RandomAccessFile;
import java.io.File;
import java.lang.reflect.Constructor;
import aed3.HashExtensivel;

class CRUDUsuario extends CRUD<Usuario> {
  
  HashExtensivel<ParEmailID> indiceIndireto;

  public CRUDUsuario(String nomeArquivo) throws Exception {
    super(nomeArquivo, Usuario.class.getConstructor());

    indiceIndireto = new HashExtensivel<>(ParEmailID.class.getConstructor(), 4, "dados/" + nomeArquivo + ".hash_d.db", "dados/" + nomeArquivo + ".hash_c.db");
  }

  @Override
  public int create(Usuario objeto) throws Exception {
    int id = super.create(objeto);
    indiceIndireto.create( new ParEmailID(objeto.getEmail(), id) );
    return id;
  }
  
  public Usuario read (String email) throws Exception{
    ParEmailID pei = new ParEmailID(email);
    int chave = pei.hashCode();
    pei = indiceIndireto.read(chave);

    if (pei == null) return null;
    return super.read(pei.getID());
  }

  @Override
  public boolean update(Usuario objeto) throws Exception {
    Usuario readObjeto = super.read(objeto.getID());
   
    if (objeto.getEmail().equals(readObjeto.getEmail())) { // email precisa ser o mesmo

      return super.update(objeto);
      
    }
    return false;
  }

  @Override
  public boolean delete(int id) throws Exception {
    // objeto precisa ser lido para gerar o hash do email e deletar o indice indireto
    Usuario objeto = super.read(id); 
    if( objeto != null && super.delete(id) ) {
      ParEmailID pei = new ParEmailID(objeto.getEmail(), id);
      return indiceIndireto.delete(pei.hashCode());
    }
    else return false;
  }
}
