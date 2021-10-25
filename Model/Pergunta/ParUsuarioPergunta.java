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

import aed3.RegistroArvoreBMais;

public class ParUsuarioPergunta implements RegistroArvoreBMais<ParUsuarioPergunta> {

  private int usuarioId;
  private int perguntaId;
  private short TAMANHO = (short) 2*Integer.BYTES;

  public ParUsuarioPergunta() {
    this(-1, -1);
  }
  public ParUsuarioPergunta(int u) {
    this(u, -1);
  }
  public ParUsuarioPergunta(int u, int p) {
    this.usuarioId=u;
    this.perguntaId=p;
  }

  public int hashCode() { return this.usuarioId; }

  public short size() { return this.TAMANHO; }

  public int getUsuario() { return this.usuarioId; }
  public void setUsuario(int value) { this.usuarioId = value; }

  public int getPergunta() { return this.perguntaId; }
  public void setPergunta(int value) { this.perguntaId = value; }

  public String toString() { return "(" + this.usuarioId + "," + this.perguntaId+ ")"; }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(usuarioId);
    dos.writeInt(perguntaId);
    return fix_size(baos.toByteArray());
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.usuarioId = dis.readInt();
    this.perguntaId = dis.readInt();
  }

  public int compareTo(ParUsuarioPergunta obj) {
    int ans = this.usuarioId - obj.getUsuario();
    if (ans == 0 && this.perguntaId != -1) ans = this.perguntaId - obj.getPergunta();
    return ans;
  }

  public ParUsuarioPergunta clone() {
    return new ParUsuarioPergunta(this.usuarioId, this.perguntaId);
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