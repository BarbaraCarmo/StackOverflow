import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class Usuario implements Registro {
  protected int idUsuario;
  protected String nome;
  protected String email;
  protected int senha;

  public Usuario(String nome, String email, String senha) throws Exception {
    this.idUsuario = -1;
    this.nome = nome;
    this.email = email;
    this.senha = senha.hashCode();
  }

  public Usuario() {
    this.idUsuario = -1;
    this.nome = "";
    this.email = "";
    this.senha = -1;
  }

  public int getID() { return this.idUsuario; }
  public void setID(int value) { this.idUsuario = value; }

  public String getEmail(){ return this.email; }
  public void setEmail(String value) { this.email = value; }

  public String getNome() { return this.nome; }
  public void setNome(String value) { this.nome = value; }

  public int getSenha() { return this.senha; }
  public void setSenha(String value) { this.senha = value.hashCode(); }
  public boolean validarSenha(String value) { return (this.senha == value.hashCode()); }

  public String toString() {
    return "\nID...: " + this.idUsuario + "\nNome.: " + this.nome + "\nEmail: " + this.email + "\nSenha: " + this.senha;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.idUsuario);
    dos.writeUTF(this.nome);
    dos.writeUTF(this.email);
    dos.writeInt(this.senha);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idUsuario = dis.readInt();
    this.nome = dis.readUTF();
    this.email = dis.readUTF();
    this.senha = dis.readInt();
  }

}