package com.example.unittesttdd.service;

import com.example.unittesttdd.model.Emprestimo;
import com.example.unittesttdd.model.Livro;
import com.example.unittesttdd.model.Usuario;
import com.example.unittesttdd.model.dto.DevolucaoResponse;
import com.example.unittesttdd.model.dto.EmprestimoResponse;
import com.example.unittesttdd.model.dto.LivroUser;
import com.example.unittesttdd.util.ServiceException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.unittesttdd.util.MsgError.*;

@Service
public class EmprestimoService {


    private static final int LIMITE_LIVROS_USER = 3;

    private final CalculoService calculoService;

    public EmprestimoService(CalculoService calculoService) {
        this.calculoService = calculoService;
    }

    public EmprestimoResponse registrarEmprestimo(Emprestimo request) throws ServiceException {
       EmprestimoResponse response =  new EmprestimoResponse();

       if(request == null){
           throw new ServiceException(EMPRESTIMO_NULL);
       }

        if(request.getUser() == null){
            throw new ServiceException(USER_NULL);
        }

       if(!podeAlugar(request.getUser())){
           throw new ServiceException(USER_LIMIT_EXCEEDED);
       }

       List<Livro> livrosReservados = reservarLivro(request.getLivros());
       LivroUser livroUser = emprestarAoUsuario(request.getUser(), livrosReservados);
       response.setUser(livroUser.getUsuario());
       response.setLivros(livroUser.getLivros());
       response.setDataEmprestimo(request.getDataEmprestimo());
       response.setDataPrevista();

       System.out.println("Usuario alugou: " + response.getUser().getNumeroDeEmprestimos() + " livro(s)");

       return response;
    }


    public DevolucaoResponse registrarDevolucao(Emprestimo emprestimo) throws ServiceException {
        DevolucaoResponse response = new DevolucaoResponse();
        AtomicReference<Double> precoAluguel = new AtomicReference<>(0.0);

        if(emprestimo == null){
            throw new ServiceException(EMPRESTIMO_NULL);
        }

        if(emprestimo.getLivros().size() <= 0){
            throw new ServiceException(DEVOLUCAO_NULL);
        }

        for(int i =0; i<emprestimo.getLivros().size(); i++){
            emprestimo.getUser().userDevolverLivro();
        }


        emprestimo.getLivros().forEach(livro -> {
            livro.devolver();
            precoAluguel.updateAndGet(v -> v + calculoService.valorAluguel(emprestimo.getDataPrevista(),
                    emprestimo.getDataDevolucao(), emprestimo.getDataEmprestimo()));

        });

        System.out.println("VALOR FINAL DE PAGAMENTO: " +precoAluguel);
        response.setDataDevolucao(emprestimo.getDataDevolucao());
        response.setLivrosDevolvidos(emprestimo.getLivros());
        response.setQtdLivrosComUser(emprestimo.getUser().getNumeroDeEmprestimos());
        response.setValorASerPago(precoAluguel.get());

        System.out.println("Devolução realizada com Sucesso - Usuario agora possui " + emprestimo.getUser().getNumeroDeEmprestimos() + " Livros alugados");

        return response;

    }

    public List<Emprestimo> consultarEmprestimosPorUsuario(Usuario locador, List<Emprestimo> emprestimos){

        List<Emprestimo> emprestimosReturn;

        emprestimosReturn = emprestimos.
                stream()
                .filter(e -> e.getUser() == locador)
                .collect(Collectors.toList());

        return emprestimosReturn;
    }

    public List<Livro> reservarLivro(List<Livro> livros) throws ServiceException {
        List<Livro> livrosReservados = new LinkedList<>(livros);

        for(Livro livro: livrosReservados){
            if(livro.isReservado()){
                System.out.println("O livro : " + livro.getTitulo() + " está reservado");
                livrosReservados.remove(livro);
                throw new ServiceException(LIVRO_RESERVADO);
            }else if(livro.isEmprestado()){
                System.out.println("O livro : " + livro.getTitulo()  + " está emprestado");
                livrosReservados.remove(livro);
                throw new ServiceException(LIVRO_EMPRESTADO);
            }
            else{
                livro.reservar();
            }
        }
        if(livros.isEmpty() || livros == null){
            throw new ServiceException(LIVROS_INDISPONÍVEIS);
        }
        return livrosReservados;
    }

    public LivroUser emprestarAoUsuario(Usuario userRequest, List<Livro> livros) throws ServiceException {
        LivroUser livroUser = new LivroUser();

        for(int i=0; i < livros.size(); i++ ){

            if(userRequest.getNumeroDeEmprestimos() >= LIMITE_LIVROS_USER){
                livros.remove(i);
                System.out.println(USER_LIMIT_EXCEEDED);
            }else{
                userRequest.realizarEmprestimos();
                livros.get(i).emprestar();
            }
        }
        livroUser.setLivros(livros);
//        livroUser.getLivros().addAll(livros);
        livroUser.setUsuario(userRequest);

        return livroUser;
    }


    /***
     * metodos auxiliares
     * */
    public boolean podeAlugar(Usuario user){
        return user.getNumeroDeEmprestimos() < LIMITE_LIVROS_USER ? true : false;
    }


//    public Double valorAluguel(LocalDateTime dataPrevista, LocalDateTime dataDevolucao, LocalDateTime dataEmprestimo){
//
//        if (dateUtil.excedeuPrazo(dataPrevista, dataDevolucao)){
//            return PRECO_FIXO_LIVRO + ( aplicarMulta( dataPrevista, dataDevolucao, dataEmprestimo ) );
//
//        }
//        return aluguelSemMulta(dateUtil.diasEntreDatas( dataDevolucao, dataEmprestimo));
//    }
//
//    public Double aluguelSemMulta(int diasEmprestados){
//        return  PRECO_FIXO_LIVRO * diasEmprestados;
//    }
//
//    public Double aplicarMulta(LocalDateTime dataPrevista, LocalDateTime dataDevolucao, LocalDateTime dataEmprestimo){
//
//        Double multa = PRECO_MULTA_LIVRO * dateUtil.diasEntreDatas(dataPrevista, dataDevolucao);
////        Double valorComMulta = multa + valorPadraoAluguel( diasEntreDatas(dataEmprestimo, dataDevolucao), qtdLivros );
//        Double limite = calcularLimite(dataEmprestimo, dataPrevista);
//
//        if(multa <= limite ){
//            return multa;
//        }
//
//        return limite;
////        return diasEntreDatas(dataPrevista, dataDevolucao) * PRECO_MULTA_LIVRO * qtdLivros;
//    }
//
//
//    public double calcularLimite(LocalDateTime dataAluguel, LocalDateTime dataPrevista/*, int qtdLivro*/){
//        /** limitada a 60% do valor do aluguel. */
//
//        Double limite;
//        int diasPrevistos = dateUtil.diasEntreDatas(dataAluguel, dataPrevista);
//        Double valorPadraoDoAluguel = aluguelSemMulta(diasPrevistos/*, qtdLivro*/);
//
//        limite = valorPadraoDoAluguel * PERCENT_LIMITED;
//        return limite;
//    }

}
