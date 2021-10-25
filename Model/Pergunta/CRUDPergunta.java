import java.io.RandomAccessFile;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Iterator;

import aed3.CRUD;
import aed3.ArvoreBMais;
import aed3.ListaInvertida;

class CRUDPergunta extends CRUD<Pergunta> {
  
  ArvoreBMais<ParUsuarioPergunta> indiceUsuario;
  ListaInvertida indiceChaves;

  public CRUDPergunta() throws Exception {
    super(Config.PERGUNTA_FILE, Pergunta.class.getConstructor());
    String nomeArquivo = Config.PERGUNTA_FILE;

    indiceUsuario = new ArvoreBMais<ParUsuarioPergunta>(ParUsuarioPergunta.class.getConstructor(), 4, "dados/" + nomeArquivo + ".abm");
    indiceChaves = new ListaInvertida(4, "dados/" + nomeArquivo + "_a.liin", "dados/" + nomeArquivo + "_b.liin");
  }
  
  @Override
  public int create(Pergunta objeto) throws Exception {
    int id = super.create(objeto);
    ParUsuarioPergunta pup = new ParUsuarioPergunta(objeto.getIDUsuario(), id);
    if (!indiceUsuario.create( pup )) return -3;
    
    String[] chaves = objeto.getPalavrasChave();
    for (int i=0; i<chaves.length; i++) {
      if (!indiceChaves.create( chaves[i], id )) return -2;
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

  // TODO
  //le pergunta por usuario
  public Pergunta[] read (Usuario usuario) throws Exception{
    ParUsuarioPergunta pup = new ParUsuarioPergunta(usuario.getID());
    List<ParUsuarioPergunta> listPup = indiceUsuario.read(pup);

    if(listPup == null) return null;
    Pergunta[] arrayPerguntas = new Pergunta[listPup.size()];
    ParUsuarioPergunta tempPup; int i=0;
    for(Iterator<ParUsuarioPergunta> li=listPup.iterator(); li.hasNext();){
      tempPup = li.next();
      arrayPerguntas[i++] = super.read(tempPup.getPergunta());
    }
    return arrayPerguntas;
  }

  @Override
  public boolean update(Pergunta objeto) throws Exception {
    Pergunta readObjeto = super.read(objeto.getID());

    // id usuario precisa ser o mesmo
    if (objeto.getIDUsuario() == readObjeto.getIDUsuario()) { 
      // atualiza indice de chaves
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
      // deleta chaves no indice de chaves
      String[] chaves = objeto.getPalavrasChave();
      for (int i=0; i<chaves.length; i++) {
        indiceChaves.delete(chaves[i], idPergunta);
      }

      // deleta pergunta na arvore de usuario
      ParUsuarioPergunta pup = new ParUsuarioPergunta(objeto.getIDUsuario(), idPergunta);
      return indiceUsuario.delete(pup);
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
