import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class AcessoMinhasPerguntas {
  String fileUsuario;
  String filePerguntas;

  CRUDUsuario crudUsuario;
  CRUDPergunta crudPergunta;

  Scanner myInput;
  Usuario usuario;
  public AcessoMinhasPerguntas(String arquivo, String arquivoPerguntas, Usuario usuario) throws Exception {
    this.usuario = usuario;
    fileUsuario = arquivo;
    filePerguntas = arquivoPerguntas;
    crudUsuario = new CRUDUsuario(arquivo);
    crudPergunta = new CRUDPergunta(arquivoPerguntas);
    myInput = new Scanner(System.in);
  }

  public boolean run() throws Exception {
    boolean status = true;

    while (status) {
      System.out.println("Menu de Usuario - " + usuario.getNome() + "\n");
      System.out.println("Home > Minha area > Minhas perguntas\n");
      System.out.println("1) Listar\n2) Incluir\n3) Alterar\n4) Arquivar\n\n0) retornar\n");
      System.out.print("Opção: ");
      int op = myInput.nextInt();
      myInput.skip("\\R");

      System.out.println();

      switch (op) {
        case 0:
          status = false;
          break;
        case 1:
          status = ListarPerguntas();
          break;
        case 2:
          status = IncluirPergunta();
          break;
        case 3:
          status = AlterarPergunta();
          break;
        case 4:
          status = ArquivarPergunta();
          break;
        default:
          return false;
      }
    }
    return true;
  }

  private boolean ListarPerguntas() throws Exception {
    Pergunta[] pergs = crudPergunta.read(usuario);
    if (pergs == null || pergs.length == 0)
      System.out.println("Nenhuma pergunta registrada.\n");
    else {
      for (int i = 0; i < pergs.length; i++) {
        System.out.println(pergs[i].toString());
      }
      System.out.println();
    }
    return true;
  }

  private boolean IncluirPergunta() throws Exception {
    String titulo = EscolheTitulo();
    if (titulo.equals("")) {
      System.out.println("\nOperação cancelada.\n");
      return true;
    }

    String[] chaves = EscolheChaves();

    Pergunta novaPergunta = new Pergunta(usuario.getID(), titulo, chaves);
    crudPergunta.create(novaPergunta);
    return true;
  }

  private boolean AlterarPergunta() throws Exception {
    Pergunta pergunta = EscolhePergunta();
    if (pergunta == null) return true; 
    
    String titulo = pergunta.getPergunta();
    if (!cancelAction("Deseja alterar o titulo?")) {
      titulo = EscolheTitulo();
      if (titulo.equals("")) {
        System.out.println("\nOperação cancelada.\n");
        return true;
      }
    }

    String[] chaves = pergunta.getPalavrasChave();
    if (!cancelAction("Deseja alterar as chaves?")) {
      chaves = EscolheChaves();
    }

    if (cancelAction("Digite sim para confirmar:")) return true;
    pergunta.setPalavrasChave(chaves);
    pergunta.setPergunta(titulo);
    crudPergunta.update(pergunta);
    return true;
  }

  private boolean ArquivarPergunta() throws Exception {
    Pergunta pergunta = EscolhePergunta();
    if (pergunta == null) return true; 

    if (cancelAction("Digite sim para confirmar mudanças:")) return true;
    pergunta.setAtiva(false);
    crudPergunta.update(pergunta);
    return true;
  }

  private Pergunta EscolhePergunta() throws Exception {
    Pergunta[] perguntasUsuario = crudPergunta.read(usuario);
    int count = 1;
    for (int i = 0; i < perguntasUsuario.length; i++) {
      if (perguntasUsuario[i].getAtiva()) {
        System.out.println("" + count + ": " + perguntasUsuario[i].toString());
        count++;
      }
    }
    System.out.println("\nQual pergunta deseja alterar?");
    int op = myInput.nextInt();
    myInput.skip("\\R");

    if (op == 0) return null;

    count = 1;
    int indexPergunta = 0;
    for (; indexPergunta<perguntasUsuario.length && count <= op; indexPergunta++) {
      
      //System.out.println(op + " - " + count + " - " + indexPergunta + " - " + perguntasUsuario[indexPergunta].getAtiva() + " - ");
      
      if (perguntasUsuario[indexPergunta].getAtiva()) count++;
    } indexPergunta--;

    System.out.println("\nPergunta selecionada:\n" + perguntasUsuario[indexPergunta].toString() + "\n");
    return perguntasUsuario[indexPergunta];
  }
  
  private String EscolheTitulo() {
    System.out.print("Digite o titulo da pergunta: ");
    String out = myInput.nextLine().trim();
    System.out.println();
    
    return out;
  }

  private String[] EscolheChaves() {
    List<String> chaves = new ArrayList<String>();
    boolean readingChaves = true;
    while (readingChaves) {
      System.out.print("Digite uma nova chave para pergunta; digite \"fim\" para terminar: ");
      String line = myInput.nextLine().trim();
      if (! line.equals("fim")) {
        chaves.add(line);
      } else
        readingChaves = false;
    }
    System.out.println();

    String[] out = new String[0];
    if (chaves.size() > 0) out = chaves.toArray(out);
    return out;
  }

  private boolean cancelAction(String confirmString) {
    System.out.println(confirmString);
    if (myInput.nextLine().equals("sim")) {
      System.out.println();
      return false;
    }
    System.out.println("\nOperação cancelada.\n");
    return true;
  }
}