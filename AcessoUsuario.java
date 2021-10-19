import java.util.Scanner;

//CORRETO: COMO ISTO É INTERFACE NENHUMA FUNÇÃO PODE RETORNAR EXCEPTION, TODAS ELAS NESTE MOMENTO VÃO MOSTRAR MENSAGENS AO USUARIO E PROVAVELMENTE FINALIZAR O PROGRAMA RETORNANDO FALSO.
// no caso tem que depois por
// try{ linha com problema }
// except{ print("menssagem especifica da linha"); return false; }
// mas não é uma boa ideia colocar isso antes de garantir que o codigo ta funcionando.
class AcessoUsuario {
  CRUDUsuario crudUsuario;
  Scanner myInput;
  String fileName;
  Usuario usuario;

  public AcessoUsuario(String arquivo, Usuario usuario) throws Exception {
    this.usuario = usuario;
    fileName = arquivo;
    crudUsuario = new CRUDUsuario(arquivo);
    myInput = new Scanner(System.in);
  }

  public boolean run() throws Exception {
    boolean status = true;

    while (status) {
      System.out.println("Menu de Usuario - " + usuario.getNome() + "\n");
      System.out.println("Home\n");
      System.out.println("1) Minha area\n2) Buscar perguntas\n\n0) Sair\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();
      myInput.skip("\\R");

      System.out.println();

      switch (op) {
        case 0:
          return true;
        case 1:
          status = MinhaArea();
          break;
        case 2:
          status = BuscarPergunta();
          break;
        default:
          return false;
      }
    }

    return true;
  }

  private boolean MinhaArea() throws Exception {
    boolean status = true;

    while (status) {
      System.out.println("Menu de Usuario - " + usuario.getNome() + "\n");
      System.out.println("Home > Minha area\n");
      System.out.println(
          "1) Minhas perguntas\n2) Minhas respostas\n3) Meus votos em perguntas\n4) Meus votos em respostas\n\n0) retornar\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();
      myInput.skip("\\R");

      System.out.println();

      switch (op) {
        case 0:
          status = false;
          break;
        case 1:
          status = new AcessoMinhasPerguntas(fileName, "perguntas", usuario).run();
          break;
        case 2:
          status = MinhasRespostas();
          break;
        case 3:
          status = VotosPerguntas();
          break;
        case 4:
          status = VotosRespostas();
          break;
        default:
          return false;
      }
    }

    return true;
  }

  private boolean BuscarPergunta() throws Exception {
    System.out.println("Nao implementado ainda.\nVolte mais tarde.\n");

    return true;
  }

  private boolean MinhasRespostas() throws Exception {
    System.out.println("Nao implementado ainda.\nVolte mais tarde.\n");

    return true;
  }

  private boolean VotosPerguntas() throws Exception {
    System.out.println("Nao implementado ainda.\nVolte mais tarde.\n");

    return true;
  }

  private boolean VotosRespostas() throws Exception {
    System.out.println("Nao implementado ainda.\nVolte mais tarde.\n");

    return true;
  }

}