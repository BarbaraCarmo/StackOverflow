/*

Esta classe representa um PAR CHAVE VALOR (PCV) 
para uma entidade Pessoa. Seu objetivo é representar
uma entrada de índice. 

Esse índice será secundário e indireto, baseado no
email de uma pessoa. Ao fazermos a busca por pessoa,
ele retornará o ID dessa pessoa, para que esse ID
possa ser buscado em um índice direto (que não é
apresentado neste projeto)

Um índice direto de ID precisaria ser criado por meio
de outra classe, cujos dados fossem um int para o ID
e um long para o endereço
 
Implementado pelo Prof. Marcos Kutova
v1.0 - 2021
 
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEmailID implements aed3.RegistroHashExtensivel<ParEmailID> {

  private String email;
  private int id;
  private short TAMANHO = 44;
  private short EMAIL_TAMANHO = 40;

  public ParEmailID() throws Exception {
    this("", -1);
  }
  public ParEmailID(String e) throws Exception {
    this(e, -1);
  }
  public ParEmailID(String e, int i) throws Exception {
    setEmail(e);
    setID(i);
  }

  public int hashCode() { return this.email.hashCode(); }

  public short size() { return this.TAMANHO; }

  public String getEmail() { return this.email; }
  public void setEmail(String value) throws Exception { 
      if (value.length() > EMAIL_TAMANHO)
        throw new Exception("Email muito grande. Tamanho: " + value.length() + " > Maximo: " + EMAIL_TAMANHO);
      this.email = value; 
    }

  public int getID() { return this.id; }
  public void setID(int value) { this.id = value; }

  public String toString() { return this.email + ";" + this.id; }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(email);
    dos.writeInt(id);
    return fix_size(baos.toByteArray());
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.email = dis.readUTF();
    this.id = dis.readInt();
  }

  private byte[] fix_size(byte[] short_data) throws IOException {
    byte[] data = new byte[TAMANHO];
    for (int i = 0; i < TAMANHO; i++)
      data[i] = ' ';
    for (int i = 0; i < short_data.length && i < TAMANHO; i++)
      data[i] = short_data[i];
    return data;
  }
}