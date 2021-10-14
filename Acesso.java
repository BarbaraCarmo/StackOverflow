import java.util.Scanner;
//CORRETO: COMO ISTO É INTERFACE NENHUMA FUNÇÃO PODE RETORNAR EXCEPTION, TODAS ELAS NESTE MOMENTO VÃO MOSTRAR MENSAGENS AO USUARIO E PROVAVELMENTE FINALIZAR O PROGRAMA RETORNANDO FALSO.
// no caso tem que depois por
// try{ linha com problema }
// except{ print("menssagem especifica da linha"); return false; }
// mas não é uma boa ideia colocar isso antes de garantir que o codigo ta funcionando.
class AcessoUsuario {
  CRUDUsuario crudArquivo;
  Scanner myInput;

  public AcessoUsuario(String arquivo) throws Exception {
    crudArquivo = new CRUDUsuario(arquivo);
    myInput = new Scanner( System.in );
  }

  public boolean run() throws Exception {
    return StartMenu();
  }

  private boolean StartMenu() throws Exception {
    
    // representa o primeiro menu que vai ser mostrado
    // local onde voce consegue registrar e entrar
    boolean status = true;

    while(status) {
      
      System.out.println("Primeiro Menu\n");
      System.out.println("1) Acesso ao sistema\n2) Novo usuário (primeiro acesso)\n3) Alteracao de senha\n\n0) Sair\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();
      // System.out.println("option read: " + op);

      switch(op) {
        case 0:
          return true;
        case 1:
          status = LogIn();
          break;
        case 2:
          status = Register();
          break;
        case 3:
          status = ChangePassword();
          break;
        default:
          return false;
      }
    }

    return status;
  }

  private boolean LogIn() throws Exception {
    if( crudArquivo.isEmpty() ) {
      System.out.println("Primeiro usuario precisa ser registrado antes do acesso.\n\n");
      return true;
    }

    // pede email e senha
    // carrega o usuario atravez do email
    // verifica senha carregada com fornecida
    // libera acesso ao menu principal
    System.out.print("Digite seu email: ");
    myInput.skip("\\R");
    String readEmail = myInput.nextLine();
    Usuario tempUser = crudArquivo.read(readEmail);

    if (tempUser == null) {
      System.out.println("\nEmail digitado não foi encontrado.\n\n");
      return true;
    }
    System.out.print("Digite sua senha: ");
    String readSenha = myInput.nextLine();
  
    if(tempUser.validarSenha(readSenha)) {
      System.out.println("\n");
      return MainMenu(tempUser.getNome());
    }
    else {
      System.out.println("\nSenha Incorreta.\n\n");
    }
    return true;
  }

  private boolean Register() throws Exception {
    // procedimento de pedir email, verificar no crud, criar usuario, etc...
    // o que explica no enunciado
    System.out.print("Digite seu email: ");
    myInput.skip("\\R");
    String readEmail = myInput.nextLine();
    
    Usuario tempUser = null;
    tempUser = crudArquivo.read(readEmail);

    if (tempUser != null) {
      System.out.println("Email já registrado.\n\n");
      return true;
    }

    System.out.print("Digite seu nome: ");
    String readNome = myInput.nextLine();
    System.out.print("Digite sua senha: ");
    String readSenha = myInput.nextLine();

    System.out.println("\nO email informado foi: " + readEmail);
    System.out.println("O nome informado foi: " + readNome);
    // Talvez não seja uma boa ideia
    System.out.println("A senha informado foi: " + readSenha);
    System.out.print("Digite \"s\" para confirmar cadastro: ");
    char validation = myInput.next().charAt(0);
    if (validation != 's') {
      System.out.println("\nOperação cancelada.\n\n");
      return true;
    }

    tempUser = new Usuario(readNome, readEmail, readSenha);
    crudArquivo.create(tempUser);
    System.out.println("\nRegistro concluido.\n\n");
    return true;
  }

    private boolean ChangePassword() throws Exception {

    System.out.print("Digite seu email: ");
    myInput.skip("\\R");
    String readEmail = myInput.nextLine();
    
    Usuario tempUser = null;
    tempUser = crudArquivo.read(readEmail);

    if (tempUser == null) {
      System.out.println("Usuario invalido.\n\n");
      return true;
    }
    
    System.out.print("Digite sua senha: ");
    String readSenha = myInput.nextLine();
    
    if(tempUser.validarSenha(readSenha)) {
      System.out.println("\n");
      System.out.print("Digite sua nova senha: ");
      String readNewSenha = myInput.nextLine();
      tempUser.setSenha(readNewSenha);
      crudArquivo.update(tempUser);

      if(crudArquivo.update(tempUser)){
        System.out.println("\nSenha alterada com sucesso.\n\n");
      }
      else{
        System.out.println("\nFalha em alterar senha.\n\n");
      }
    }
    else {
      System.out.println("\nSenha Incorreta.\n\n");
    }

    return true;
  }

  private boolean MainMenu(String username) throws Exception {
    boolean status = true;

    while(status) {
      System.out.println("Menu de Usuario - " + username+ "\n");
      System.out.println("1) Continuar no sistema\n\n0) Sair\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();

      switch(op) {
        case 0:
          return true;
        case 1:
          
          System.out.println("Nada alem de login.\nVolte mais tarde.\n\n");
          status = true;
          break;
        default:
          return false;
      }
    }

    return status;
  }

  
}
