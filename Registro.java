// package aed3;

public interface Registro {
  public byte[] toByteArray() throws Exception;
  public void fromByteArray(byte[] ba) throws Exception;

  public int getID();
  public void setID(int ba);
}
