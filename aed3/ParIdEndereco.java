/*

Esta classe representa um PAR CHAVE VALOR (PCV) 
para uma entidade Pessoa. Seu objetivo é representar
uma entrada de índice. 

Esse índice será direto.
 
Implementado pelo Prof. Marcos Kutova
v1.0 - 2021
 
*/
package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdEndereco implements RegistroHashExtensivel<ParIdEndereco> {

  private int id;
  private long endereco;
  private short TAMANHO = 12;

  public ParIdEndereco() {
    this(-1, -1);
  }

  public ParIdEndereco(int i) {
    this(i, -1);
  }

  public ParIdEndereco(int i, long e) {
    this.id = i;
    this.endereco = e;
  }

  public int hashCode() { return this.id; }

  public short size() { return this.TAMANHO; }

  public int getID() { return this.id; }
  public void setID(int value) { this.id = value; }

  public long getEndereco() { return this.endereco; }
  public void setEndereco(long value) { this.endereco = value; }

  public String toString() { return "(" + this.id + "," + this.endereco + ")"; }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id);
    dos.writeLong(this.endereco);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id = dis.readInt();
    this.endereco = dis.readLong();
  }

}