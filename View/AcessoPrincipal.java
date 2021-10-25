//CORRETO: COMO ISTO É INTERFACE NENHUMA FUNÇÃO PODE RETORNAR EXCEPTION, TODAS ELAS NESTE MOMENTO VÃO MOSTRAR MENSAGENS AO USUARIO E PROVAVELMENTE FINALIZAR O PROGRAMA RETORNANDO FALSO.
// no caso tem que depois por
// try{ linha com problema }
// except{ print("menssagem especifica da linha"); return false; }
// mas não é uma boa ideia colocar isso antes de garantir que o codigo ta funcionando.
import java.util.Scanner;

import Model.Usuario.*;

class AcessoPrincipal {
  CRUDUsuario crudUsuario;
  Scanner myInput;

  public AcessoPrincipal() throws Exception {
    crudUsuario = new CRUDUsuario();
    myInput = new Scanner( System.in );
  }

  public boolean run() throws Exception {
    // representa o primeiro menu que vai ser mostrado
    // local onde voce consegue registrar e entrar
    boolean status = true;

    while(status) {
      
      System.out.println("Primeiro Menu\n");
      System.out.println("1) Acesso ao sistema\n2) Novo usuário (primeiro acesso)\n3) Alteracao de senha\n\n0) Sair\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();
      myInput.skip("\\R");
      
      System.out.println();

      switch(op) {
      case 0:
        status = false;
        break;
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

    return true;
  }

  private boolean LogIn() throws Exception {
    if( crudUsuario.isEmpty() ) {
      System.out.println("Primeiro usuario precisa ser registrado antes do acesso.\n\n");
      return true;
    }

    // pede email e senha
    // carrega o usuario atravez do email
    // verifica senha carregada com fornecida
    // libera acesso ao menu principal
    System.out.print("Digite seu email: ");
    String readEmail = myInput.nextLine();
    Usuario tempUser = crudUsuario.read(readEmail);

    if (tempUser == null) {
      System.out.println("\nEmail digitado não foi encontrado.\n\n");
      return true;
    }
    System.out.print("Digite sua senha: ");
    String readSenha = myInput.nextLine();
  
    if(tempUser.validarSenha(readSenha)) {
      System.out.println("\n");
      return new AcessoUsuario(tempUser).run();
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
    String readEmail = myInput.nextLine();
    
    Usuario tempUser = null;
    tempUser = crudUsuario.read(readEmail);

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
    crudUsuario.create(tempUser);
    System.out.println("\nRegistro concluido.\n\n");
    return true;
  }

    private boolean ChangePassword() throws Exception {

    System.out.print("Digite seu email: ");
    String readEmail = myInput.nextLine();
    
    Usuario tempUser = null;
    tempUser = crudUsuario.read(readEmail);

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
      crudUsuario.update(tempUser);

      if(crudUsuario.update(tempUser)){
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

  
}
