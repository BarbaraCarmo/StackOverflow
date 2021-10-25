import java.io.RandomAccessFile;
import java.io.File;
import java.lang.reflect.Constructor;

import aed3.CRUD;
import aed3.HashExtensivel;

class CRUDUsuario extends CRUD<Usuario> {
  HashExtensivel<ParEmailUsuario> indiceIndireto;

  public CRUDUsuario() throws Exception {
    super(Config.USUARIO_FILE, Usuario.class.getConstructor());
    String nomeArquivo = Config.USUARIO_FILE;
    
    indiceIndireto = new HashExtensivel<>(ParEmailUsuario.class.getConstructor(), 4, "dados/" + nomeArquivo + "_c.hash", "dados/" + nomeArquivo + "_d.hash");
  }

  @Override
  public int create(Usuario objeto) throws Exception {
    int usuarioId = super.create(objeto);
    indiceIndireto.create( new ParEmailUsuario(objeto.getEmail(), usuarioId) );
    return usuarioId;
  }
  
  public Usuario read (String email) throws Exception{
    ParEmailUsuario peu = new ParEmailUsuario(email);
    int chave = peu.hashCode();
    peu = indiceIndireto.read(chave);

    if (peu == null) return null;
    return super.read(peu.getID());
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
  public boolean delete(int usuarioId) throws Exception {
    // objeto precisa ser lido para gerar o hash do email e deletar o indice indireto
    Usuario objeto = super.read(usuarioId); 
    if( objeto != null && super.delete(usuarioId) ) {
      ParEmailUsuario peu = new ParEmailUsuario(objeto.getEmail(), usuarioId);
      return indiceIndireto.delete(peu.hashCode());
    }
    else return false;
  }
}
