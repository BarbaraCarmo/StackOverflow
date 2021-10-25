import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import aed3.Registro;

public class Pergunta implements Registro {
  protected int idPergunta;
  protected int idUsuario;
  protected long criacao;
  protected short nota;
  protected String pergunta;
  protected String[] palavrasChave;
  protected boolean ativa;

  public Pergunta(int idUsuario, long criacao, short nota, String pergunta, String[] palavrasChave, boolean ativa) throws Exception {
    this.idPergunta = -1;
    this.idUsuario = idUsuario;
    this.criacao = criacao;
    this.nota = nota; 
    this.pergunta = pergunta;
    this.palavrasChave = palavrasChave;
    this.ativa = ativa;
  }

  public Pergunta() throws Exception {
    this.idPergunta = -1;
    this.idUsuario = -1;
    this.criacao = -1;
    this.nota = -1;
    this.pergunta = "";
    this.palavrasChave = new String[0];
    this.ativa = false;
  }

  public Pergunta(int idUsuario, String pergunta, String[] chaves) throws Exception {
    this(idUsuario, new Date().getTime(), (short)0, pergunta, chaves, true);
  }

  public int getID() { return this.idPergunta; }
  public void setID(int value) { this.idPergunta = value; }

  public int getIDPergunta() { return this.idPergunta; }
  public void setIDPergunta(int value) { this.idPergunta = value; }

  public int getIDUsuario() { return this.idUsuario; }
  public void setIDUsuario(int value) { this.idUsuario = value; }

  public long getCriacao() {return this.criacao;}
  public void setCriacao(long value) {this.criacao = value;}

  public short getNota() {return this.nota;}
  public void setNota(short value) {this.nota = value;}

  public String getPergunta() {return this.pergunta; }
  public void setPergunta(String value) {this.pergunta = value;}

  public String[] getPalavrasChave() {return this.palavrasChave;}
  public void setPalavrasChave(String[] value) {this.palavrasChave = value;}

  public boolean getAtiva() {return this.ativa;}
  public void setAtiva(boolean value) {this.ativa = value;}
    
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    Timestamp ts = new Timestamp(this.criacao);
    
    String palavraChaveString = "";
    if (this.palavrasChave.length > 0) {
      for(int i = 0; i < this.palavrasChave.length-1; i++){
        palavraChaveString += palavrasChave[i] + ", ";
      }
      palavraChaveString += palavrasChave[this.palavrasChave.length-1] ;
    }

    return "\nIdPergunta: " + this.idPergunta + "\nIdUsuario: " + this.idUsuario + "\nCriação: " + sdf.format(ts)
        + "\nNota: " + this.nota + "\nPergunta: " + this.pergunta + "\nPalavras Chaves: " + palavraChaveString
        + "\nAtiva: " + this.ativa;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.idPergunta);
    dos.writeInt(this.idUsuario);
    dos.writeLong(this.criacao);
    dos.writeShort(this.nota);
    dos.writeUTF(this.pergunta);
    dos.writeInt(this.palavrasChave.length);
    for (int i=0; i<this.palavrasChave.length; i++) {
      dos.writeUTF(this.palavrasChave[i]);
    }
    dos.writeBoolean(this.ativa);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idPergunta = dis.readInt();
    this.idUsuario = dis.readInt();
    this.criacao = dis.readLong();
    this.nota = dis.readShort();
    this.pergunta = dis.readUTF();
    int palavrasSize = dis.readInt();
    this.palavrasChave = new String[palavrasSize];
    for (int i=0; i<this.palavrasChave.length; i++) {
      this.palavrasChave[i] = dis.readUTF();
    }
    this.ativa = dis.readBoolean();
  }

}