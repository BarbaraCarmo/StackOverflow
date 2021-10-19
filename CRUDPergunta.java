import java.io.RandomAccessFile;
import java.io.File;
import java.lang.reflect.Constructor;
import aed3.ListaInvertida;

class CRUDPergunta extends CRUD<Pergunta> {
  
  ListaInvertida indiceUsuario;
  ListaInvertida indiceChaves;

  public CRUDPergunta(String nomeArquivo) throws Exception {
    super(nomeArquivo, Pergunta.class.getConstructor());

    indiceUsuario = new ListaInvertida(4, "dados/" + nomeArquivo + ".liin_d.db", "dados/" + nomeArquivo + ".liin_c.db");
    indiceChaves = new ListaInvertida(4, "dados/" + nomeArquivo + ".liin_d.db", "dados/" + nomeArquivo + ".liin_c.db");
  }

  @Override
  public int create(Pergunta objeto) throws Exception {
    int id = super.create(objeto);
    indiceUsuario.create( Integer.toString(objeto.getIDUsuario()), id );
    
    String[] chaves = objeto.getPalavrasChave();
    for (int i=0; i<chaves.length; i++) {
      indiceChaves.create( chaves[i], id );
    }

    return id;
  }
  
  //le pergunta pela chave
  public Pergunta[] read (String chave) throws Exception{
    int[] ip = indiceChaves.read(chave);

    if(ip == null) return null;
    Pergunta[] arrayPerguntas = new Pergunta[ip.length];
    for(int i=0; i<ip.length; i++){
      arrayPerguntas[i] = super.read(ip[i]);
    }
    return arrayPerguntas;
  }

  //le pergunta por usuario
  public Pergunta[] read (Usuario usuario) throws Exception{
   
    int[] ip = indiceUsuario.read(Integer.toString(usuario.getID()));

    if(ip == null) return null;
    Pergunta[] arrayPerguntas = new Pergunta[ip.length];
    for(int i=0; i<ip.length; i++){
      arrayPerguntas[i] = super.read(ip[i]);
    }
    return arrayPerguntas;
  }

  @Override
  public boolean update(Pergunta objeto) throws Exception {
    Pergunta readObjeto = super.read(objeto.getID());
   
    if (objeto.getIDUsuario() == readObjeto.getIDUsuario()) { // id usuario precisa ser o mesmo
      updateIndiceChaves(objeto.getPalavrasChave(), readObjeto.getPalavrasChave(), objeto.getID());
      return super.update(objeto);
    }
    return false;
  }

  @Override
  public boolean delete(int idPergunta) throws Exception {
    // objeto precisa ser lido para gerar o hash do email e deletar o indice indireto
    Pergunta objeto = super.read(idPergunta); 
    if( objeto != null && super.delete(idPergunta) ) {
      return indiceUsuario.delete(Integer.toString(objeto.getIDUsuario()),idPergunta);
    }
    else return false;
  }

  private void updateIndiceChaves(String[] chaves_new, String[] chaves_old, int objetoId) throws Exception {
    for (int i=0; i<chaves_new.length; i++) {
      boolean found = false;
      for (int j=0; j<chaves_old.length; j++) {
        if (chaves_new == chaves_old) {
          found = true;
          chaves_old[j] = null;
          break;
        }
      }

      if (!found) {
        indiceChaves.create(chaves_new[i], objetoId);
      }
    }
    
    for (int i=0; i<chaves_old.length; i++) {
      if (chaves_old[i] != null) indiceChaves.delete(chaves_old[i], objetoId);
    }
  }
}
