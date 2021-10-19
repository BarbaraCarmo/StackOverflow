public class Main {
  public static void main(String[] args) {

    try {
      CRUDUsuario crudu = new CRUDUsuario("usuarios");
      AcessoUsuario au = new AcessoUsuario("usuarios", crudu.read(1));
      au.run();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
