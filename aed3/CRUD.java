package aed3;

import java.io.RandomAccessFile;
import java.io.File;
import java.lang.reflect.Constructor;

public class CRUD<T extends Registro> {

  RandomAccessFile arquivo;
  Constructor<T> construtor;
  final int TAMANHO_CABECALHO = 4;
  HashExtensivel<ParIdEndereco> myHash;

  public CRUD(String nomeArquivo, Constructor<T> c) throws Exception {
    createFolders(nomeArquivo);
    
    arquivo = new RandomAccessFile("dados/" + nomeArquivo + ".db", "rw");
    construtor = c;
    if (arquivo.length() == 0) {
      arquivo.writeInt(0);
    }
    myHash = new HashExtensivel<>(ParIdEndereco.class.getConstructor(), 4, "dados/" + nomeArquivo + "_a.hash", "dados/" + nomeArquivo + "_b.hash");
  }

  private void createFolders(String nomeArquivo) throws Exception {
    String[] names = nomeArquivo.split("/");
    String folderString = "dados/";
    
    if (names.length > 1) {
      for (int i=0; i<names.length-1; i++) folderString += names[i];
    }

    File f = new File(folderString);
    if (!f.exists()) {
      f.mkdirs();
    }
  }

  public boolean isEmpty() throws Exception { 
    arquivo.seek(0);
    return arquivo.readInt() == 0;
  }

  public int create(T obj) throws Exception {
    arquivo.seek(0);
    int ultimoID = arquivo.readInt();
    int proximoID = ultimoID + 1;
    arquivo.seek(0);
    arquivo.writeInt(proximoID);

    arquivo.seek(arquivo.length());
    obj.setID(proximoID);
    byte[] byArray = obj.toByteArray();

    myHash.create(new ParIdEndereco(proximoID, arquivo.length()));

    arquivo.writeByte(' ');
    arquivo.writeInt(byArray.length);
    arquivo.write(byArray);

    return proximoID;
  }


  public T read(int idProcurado) throws Exception {
    ParIdEndereco parIDEnd;
    parIDEnd = myHash.read(idProcurado);

    if (parIDEnd != null) {
      arquivo.seek(parIDEnd.getEndereco());
      byte lapide = arquivo.readByte();
      int tam = arquivo.readInt();
      T obj = construtor.newInstance();
      byte[] byArray;
      if (lapide == ' ') {
        byArray = new byte[tam];
        arquivo.read(byArray);
        obj.fromByteArray(byArray);

        if (obj.getID() == idProcurado) {
          return obj;
        }
      }
    }
    return null;
  }

  public boolean update(T obj) throws Exception {
    ParIdEndereco parIDEnd;
    parIDEnd = myHash.read(obj.getID());
    
    arquivo.seek(parIDEnd.getEndereco());
    byte lapide = arquivo.readByte();
    int tam = arquivo.readInt();
    T tempObj = construtor.newInstance();
    byte[] ba;
    byte[] objByArray = obj.toByteArray();

    if (lapide == ' ') {
      ba = new byte[tam];
      arquivo.read(ba);
      tempObj.fromByteArray(ba);

      if (obj.getID() == tempObj.getID()) {

        if (objByArray.length <= ba.length) {
          arquivo.seek(arquivo.getFilePointer() - tam);
          arquivo.write(objByArray);
          return true;
        } else {
          arquivo.seek(arquivo.getFilePointer() - tam - 5);
          arquivo.writeByte('*');
          arquivo.seek(arquivo.length());

          myHash.update(new ParIdEndereco(obj.getID(), arquivo.length()));

          arquivo.writeByte(' ');
          arquivo.writeInt(objByArray.length);
          arquivo.write(objByArray);
          return true;
        }
      }
    }
    return false;
  }

  public boolean delete(int idProcurado) throws Exception {
    ParIdEndereco parIDEnd;
    parIDEnd = myHash.read(idProcurado);
    
    arquivo.seek(parIDEnd.getEndereco());
    byte lapide = arquivo.readByte();
    int tam = arquivo.readInt();
    T tempObj = construtor.newInstance();
    byte[] ba;

    if (lapide == ' ') {
      ba = new byte[tam];
      arquivo.read(ba);
      tempObj.fromByteArray(ba);

      if (idProcurado == tempObj.getID()) {
        arquivo.seek(arquivo.getFilePointer() - tam - 5);
        arquivo.writeByte('*');
        myHash.delete(idProcurado);
        return true;
      }
    }
    return false;
  }

}