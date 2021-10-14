import java.io.File;

// import aed3.ArquivoIndexado;

public class Main {
  public static void main(String[] args) {

    // remover em um código real
    // File f = new File("dados/Usuarios/arquivo.db");
    // f.delete();
    // f = new File("dados/Usuarios/indiceDireto_d.db");
    // f.delete();
    // f = new File("dados/Usuarios/indiceDireto_c.db");
    // f.delete();
    // f = new File("dados/clientes/arquivo.db");
    // f.delete();
    // f = new File("dados/clientes/indiceDireto_d.db");
    // f.delete();
    // f = new File("dados/clientes/indiceDireto_c.db");
    // f.delete();

    // ArquivoIndexado<Usuario> arqUsuarios;

    try {
      AcessoUsuario au = new AcessoUsuario("usuarios");
      au.run();
      // System.out.println("PERGUNTAS 1.0 \n=============\n");
      // System.out.println("ACESSO\n");
      // System.out.println("1) Acesso ao sistema\n2) Novo usuário (primeiro acesso)\n\n0) Sair\n");
      // System.out.print("Opção:");

      //Usuario l1 = new Usuario("Eu, Robô", "Isaac Asimov", "9788576572008", 14.90F);
      //Usuario l2 = new Usuario("Eu Sou a Lenda", "Richard Matheson", "9788576572718", 21.99F);
      

      // ESCRITA
      //arqUsuarios = new ArquivoIndexado<>("Usuarios", //Usuario.class.getConstructor());
      

      //arqUsuarios.create(l1);
      //arqUsuarios.create(l2);
      
      //Usuario l0 = arqUsuarios.read(2);
      //System.out.println(l0);

      //arqUsuarios.delete(2);
      //System.out.println("\n" + arqUsuarios.read(2));

      //c0.nome = "José das Couves";
      //arqUsuarios.update(c0);
      //System.out.println("\n" + arqUsuarios.read(1));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}