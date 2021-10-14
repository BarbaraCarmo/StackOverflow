// package aed3;

// import java.io.RandomAccessFile;
// import java.io.File;
// import java.lang.reflect.Constructor;

// public class ArquivoIndexado<T extends Registro> {

//   RandomAccessFile arquivo;
//   HashExtensivel<ParIDEndereco> indiceDireto;
//   Constructor<T> construtor;
//   final int TAMANHO_CABECALHO = 4;

//   public ArquivoIndexado(String nomeEntidade, Constructor<T> c) throws Exception {

//     File f = new File("dados");
//     if (!f.exists()) {
//       f.mkdir();
//     }
//     f = new File("dados/" + nomeEntidade);
//     if (!f.exists()) {
//       f.mkdir();
//     }
//     arquivo = new RandomAccessFile("dados/" + nomeEntidade + "/arquivo.db", "rw");
//     indiceDireto = new HashExtensivel<>(ParIDEndereco.class.getConstructor(), 4,
//         "dados/" + nomeEntidade + "/indiceDireto_d.db", "dados/" + nomeEntidade + "/indiceDireto_c.db");
//     construtor = c;
//     if (arquivo.length() == 0) {
//       arquivo.writeInt(0);
//     }
//   }

//   public int create(T obj) throws Exception {

//     // Obtem o último ID usado no cabeçalho do arquivo
//     arquivo.seek(0);
//     int ultimoID = arquivo.readInt();

//     // Incrementa esse ID para ser usado no próximo registro
//     // e atualiza o cabeçalho
//     int proximoID = ultimoID + 1;
//     arquivo.seek(0);
//     arquivo.writeInt(proximoID);

//     // Move para o fim do arquivo para inserir o novo registro
//     arquivo.seek(arquivo.length());
//     long endereco = arquivo.getFilePointer();

//     // Insere o registro no arquivo
//     obj.setID(proximoID);
//     byte[] ba = obj.toByteArray();
//     arquivo.writeByte(' '); // lápide
//     arquivo.writeInt(ba.length); // tamanho do vetor de bytes
//     arquivo.write(ba); // vetor de bytes (representando os dados da entidade)

//     // Insere o par ID, Endereço no índice direto
//     indiceDireto.create(new ParIDEndereco(obj.getID(), endereco));

//     return proximoID;
//   }

//   public T read(int idProcurado) throws Exception {

//     arquivo.seek(TAMANHO_CABECALHO); // Pular o cabeçalho e se posicionar no primeiro registro
//     byte lapide;
//     int tam;
//     T obj = construtor.newInstance();
//     byte[] ba;

//     ParIDEndereco par = indiceDireto.read(idProcurado);
//     if (par == null)
//       return null;
//     long endereco = par.getEndereco();
//     if (endereco == -1)
//       return null;

//     // Lê o registro no arquivo
//     arquivo.seek(endereco);
//     lapide = arquivo.readByte(); // lê o lápide
//     tam = arquivo.readInt(); // lê o tamanho do vetor de bytes
//     if (lapide == ' ') { // testa se o registro não está excluído
//       ba = new byte[tam];
//       arquivo.read(ba); // lê o vetor de bytes
//       obj.fromByteArray(ba); // extrai a entidade do vetor de bytes
//       if (obj.getID() == idProcurado) // testa se é a entidade procurada (pelo ID)
//         return obj;
//     }
//     return null;
//   }

//   public boolean delete(int idProcurado) throws Exception {

//     arquivo.seek(TAMANHO_CABECALHO); // Pular o cabeçalho e se posicionar no primeiro registro
//     byte lapide;
//     int tam;
//     T obj = construtor.newInstance();
//     byte[] ba;

//     ParIDEndereco par = indiceDireto.read(idProcurado);
//     if (par == null)
//       return false;
//     long endereco = par.getEndereco();
//     if (endereco == -1)
//       return false;

//     // Lê o registro no arquivo
//     arquivo.seek(endereco);
//     lapide = arquivo.readByte(); // lê o lápide
//     tam = arquivo.readInt(); // lê o tamanho do vetor de bytes
//     if (lapide == ' ') { // testa se o registro não está excluído
//       ba = new byte[tam];
//       arquivo.read(ba); // lê o vetor de bytes
//       obj.fromByteArray(ba); // extrai a entidade do vetor de bytes
//       if (obj.getID() == idProcurado) { // testa se é a entidade procurada (pelo ID)
//         arquivo.seek(endereco);
//         arquivo.writeByte('*');
//         indiceDireto.delete(idProcurado);
//         return true;
//       }
//     }
//     return false;
//   }

//   public boolean update(T novoObj) throws Exception {

//     arquivo.seek(TAMANHO_CABECALHO); // Pular o cabeçalho e se posicionar no primeiro registro
//     byte lapide;
//     int tam;
//     T obj = construtor.newInstance();
//     byte[] ba;
//     int idProcurado = novoObj.getID();

//     ParIDEndereco par = indiceDireto.read(idProcurado);
//     if (par == null)
//       return false;
//     long endereco = par.getEndereco();
//     if (endereco == -1)
//       return false;

//     // Lê o registro no arquivo
//     arquivo.seek(endereco);
//     lapide = arquivo.readByte(); // lê o lápide
//     tam = arquivo.readInt(); // lê o tamanho do vetor de bytes
//     if (lapide == ' ') { // testa se o registro não está excluído
//       ba = new byte[tam];
//       arquivo.read(ba); // lê o vetor de bytes
//       obj.fromByteArray(ba); // extrai a entidade do vetor de bytes
//       if (obj.getID() == idProcurado) { // testa se é a entidade procurada (pelo ID)
//         byte[] novoBA = novoObj.toByteArray();
//         int novoTam = novoBA.length;
//         if (novoTam <= tam) { // vetor de bytes NÃO cresceu; o registro pode ficar no mesmo lugar
//           arquivo.seek(endereco + 5); // saltando o lápido e o indicador de tamanho do registro
//           arquivo.write(novoBA);
//         } else { // vetor de bytes cresceu e o registro precisa ser movido para o fim do arquivo
//           arquivo.seek(endereco); // volta para o início do registro
//           arquivo.writeByte('*'); // marca o lápide (registro excluído)
//           arquivo.seek(arquivo.length()); // move o ponteiro para o fim do arquivo
//           long enderecoNovo = arquivo.getFilePointer();
//           arquivo.writeByte(' ');
//           arquivo.writeInt(novoTam);
//           arquivo.write(novoBA);
//           indiceDireto.update(new ParIDEndereco(idProcurado, enderecoNovo));
//         }
//         return true;
//       }
//     }
//     return false;
//   }

// }
