package com.example.unittesttdd.service;

import com.example.unittesttdd.model.Emprestimo;
import com.example.unittesttdd.model.Livro;
import com.example.unittesttdd.model.Usuario;
import com.example.unittesttdd.model.dto.DevolucaoResponse;
import com.example.unittesttdd.model.dto.EmprestimoResponse;
import com.example.unittesttdd.util.DateUtil;
import com.example.unittesttdd.util.ServiceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.example.unittesttdd.util.MsgError.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    private static final String LIVRO1 = "O CODIGO LIMPO";
    private static final String LIVRO2 = "HISTORIA DA COMPUTAÇÃO";
    private static final String LIVRO3 = "ALGORITMO E LOGICA DE PROGRAMAÇÃO";
    private static final String LIVRO4 = "TDD - DESENVOLVIMENTO GUIADO POR TESTES";

    private static final LocalDateTime DEZ_OUT_01 = LocalDateTime.of(2022, 10, 1, 10, 20);
    private static final LocalDateTime DEZ_OUT_08 = LocalDateTime.of(2022, 10, 8, 10, 20);
    private static final LocalDateTime DEZ_OUT_10 = LocalDateTime.of(2022, 10, 10, 10, 20);
    private static final LocalDateTime DEZ_OUT_15 = LocalDateTime.of(2022, 10, 15, 10, 20);
    private static final LocalDateTime DEZ_OUT_17 = LocalDateTime.of(2022, 10, 17, 10, 20);
    private static final LocalDateTime DEZ_OUT_18 = LocalDateTime.of(2022, 10, 18, 10, 20);


    @InjectMocks
    private EmprestimoService emprestimoService;
    @InjectMocks
    private CalculoService calculoService;
    @InjectMocks
    private DateUtil dateUtil;


    @BeforeEach
    public void init(){
        calculoService = new CalculoService(dateUtil);
        emprestimoService = new EmprestimoService(calculoService);
    }

    @Test
    @DisplayName("deve realizar emprestimo em livro que nao esteja reservado e ao termino os livros devem ser reservados")
    public void alugar() throws ServiceException {
        /**CENÁRIO*/
        Emprestimo emprestimoMock = gerarEmprestimo(gerarUser("EDGAR", "20182SI0027"));
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros(getLivros(false, false));

        /**AÇÃO*/
        EmprestimoResponse emprestimoRealizado = emprestimoService.registrarEmprestimo(emprestimoMock);


        /**ASSERTIVAS*/
        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isReservado() ) );
        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isEmprestado() ) );

        assertEquals(emprestimoRealizado.getLivros().size(), emprestimoMock.getLivros().size());
        assertEquals(emprestimoRealizado.getUser(), emprestimoMock.getUser());
        System.out.println("TESTE 1 SUCCESS");

    }


    @Test
    @DisplayName("Tentativa de empréstimo em livro que já possui uma reserva")
    public void alugarComReserva(){
        /**CENÁRIO*/
        Emprestimo emprestimoMock = gerarEmprestimo(gerarUser("EDGAR", "20182SI0027"));
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros(getLivros(true, false));

        /**AÇÃO*/

        /**ASSERTIVAS*/

        Assertions.assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> emprestimoService.registrarEmprestimo( emprestimoMock ))
                .withMessage(LIVRO_RESERVADO);
        System.out.println("TESTE 2 SUCCESS");

    }

    @Test
    @DisplayName("deve Garantir que a data prevista esteja correta, após a locação de um livro")
    public void verificarDataPrevista() throws ServiceException {
        /**CENÁRIO*/
        Emprestimo emprestimoMock = gerarEmprestimo(gerarUser("EDGAR", "20182SI0027"));
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros( Arrays.asList(gerarLivro( LIVRO3,false, false)));

        /**AÇÃO*/
        EmprestimoResponse emprestimoRealizado = emprestimoService.registrarEmprestimo(emprestimoMock);


        /**ASSERTIVAS*/
        assertEquals(emprestimoRealizado.getDataPrevista(), DEZ_OUT_08);
        System.out.println("TESTE 3 SUCCESS");

    }

    @Test
    @DisplayName("Testa um usuário sem empréstimo")
    public void usuarioSemimprestimo(){
        /**CENÁRIO*/
        Usuario usermock = gerarUser("Joao", "20192SI0014");


        /**AÇÃO*/

        boolean podeAlugar = emprestimoService.podeAlugar(usermock);

        /**ASSERTIVAS*/
        assertTrue(podeAlugar);
        System.out.println("TESTE 4 SUCCESS");

    }

    @Test
    @DisplayName("Testa um usuário com 1 empréstimo")
    public void usuarioCom_1_Emprestimo() throws ServiceException {
        /**CENÁRIO*/
        Usuario usermock = gerarUser("Joao", "20192SI0014");
        Emprestimo emprestimoMock = gerarEmprestimo(usermock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros( Arrays.asList(gerarLivro( LIVRO2,false, false)));


        /**AÇÃO*/

        boolean podeAlugar = emprestimoService.podeAlugar(usermock);
        EmprestimoResponse emprestimoRealizado = emprestimoService.registrarEmprestimo(emprestimoMock);

        /**ASSERTIVAS*/
        assertTrue(podeAlugar);

        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isReservado() ) );
        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isEmprestado() ) );

        assertEquals( emprestimoRealizado.getLivros().size(), emprestimoMock.getLivros().size() );
        assertEquals( emprestimoRealizado.getUser().getNumeroDeEmprestimos() , 1 );
        System.out.println("TESTE 5 SUCCESS");

    }

    @Test
    @DisplayName("Testa um usuário com 3 empréstimos")
    public void usuarioCom_3_Emprestimo() throws ServiceException {
        /**CENÁRIO*/
        Usuario usermock = gerarUser("Joao", "20192SI0014");
        Emprestimo emprestimoMock = gerarEmprestimo(usermock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros( getLivros(false, false) );


        /**AÇÃO*/
        boolean podeAlugar_Before = emprestimoService.podeAlugar(usermock);
        EmprestimoResponse emprestimoRealizado = emprestimoService.registrarEmprestimo(emprestimoMock);
        boolean podeAlugar_After = emprestimoService.podeAlugar(emprestimoRealizado.getUser());


        /**ASSERTIVAS*/

        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isReservado() ) );
        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isEmprestado() ) );

        assertEquals( emprestimoRealizado.getLivros().size(), emprestimoMock.getLivros().size() );
        assertEquals( emprestimoRealizado.getUser().getNumeroDeEmprestimos() , 3 );

        assertTrue( podeAlugar_Before );
        assertFalse( podeAlugar_After );
        System.out.println("TESTE 6 SUCCESS");

    }

    @Test
    @DisplayName("Testa de realizar um 4º empréstimo para o mesmo usuário")
    public void usuarioCom_4_Emprestimo() throws ServiceException {
        /**CENÁRIO*/
        Usuario usermock = gerarUser("Romulo", "20192SI0012");
        Emprestimo emprestimoMock = gerarEmprestimo(usermock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_01);
        emprestimoMock.setLivros( getLivros(false, false) );
        emprestimoMock.getLivros().add(gerarLivro(LIVRO4, false, false));

        /**AÇÃO*/
        boolean podeAlugar_Before = emprestimoService.podeAlugar(usermock);
        EmprestimoResponse emprestimoRealizado = emprestimoService.registrarEmprestimo(emprestimoMock);
        boolean podeAlugar_After = emprestimoService.podeAlugar(emprestimoRealizado.getUser());

        /**ASSERTIVAS*/

        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isReservado() ) );
        assertTrue( emprestimoRealizado.getLivros().stream().allMatch( e -> e.isEmprestado() ) );

        assertEquals( emprestimoRealizado.getLivros().size(), 3 );
        assertEquals( emprestimoRealizado.getUser().getNumeroDeEmprestimos() , 3 );

        assertTrue( podeAlugar_Before );
        assertFalse( podeAlugar_After );

        Assertions.assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> emprestimoService.registrarEmprestimo( emprestimoMock ))
                .withMessage(USER_LIMIT_EXCEEDED);
        System.out.println("TESTE 7 SUCCESS");

    }

    @Test
    @DisplayName("Testa uma devolução antes da data prevista")
    public void devolucaoAntecipada() throws ServiceException {
        /**CENÁRIO*/
        Usuario userMock = gerarUser("Tarcysio", "2010SI201457");
        Emprestimo emprestimoMock = gerarEmprestimo(userMock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_10);
        emprestimoMock.setDataPrevista();
        emprestimoMock.setDataDevolucao(DEZ_OUT_15);
        emprestimoMock.setLivros( getLivros(true, true) );
        emprestimoMock.getUser().setNumeroDeEmprestimos(emprestimoMock.getLivros().size());


        /**AÇÃO*/
        DevolucaoResponse devolucao = emprestimoService.registrarDevolucao(emprestimoMock);


        /**ASSERTIVAS*/
        assertEquals(emprestimoMock.getLivros().size(), devolucao.getLivrosDevolvidos().size());
        assertEquals(0, devolucao.getQtdLivrosComUser());
        assertEquals(75.0, devolucao.getValorASerPago());

        assertTrue( devolucao.getDataDevolucao()
                .isBefore(emprestimoMock.getDataPrevista()) );
        System.out.println("TESTE 8 SUCCESS");

    }

    @Test
    @DisplayName("Testa uma devolução na data prevista")
    public void devolucaoDataPrevista() throws ServiceException {
        /**CENÁRIO*/
        Usuario userMock = gerarUser("Marcio", "2010SI201457");
        Emprestimo emprestimoMock = gerarEmprestimo(userMock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_10);
        emprestimoMock.setDataPrevista();
        emprestimoMock.setDataDevolucao(DEZ_OUT_17);
        emprestimoMock.setLivros( getLivros(true, true) );
        emprestimoMock.getUser().setNumeroDeEmprestimos(emprestimoMock.getLivros().size());


        /**AÇÃO*/
        DevolucaoResponse devolucao = emprestimoService.registrarDevolucao(emprestimoMock);


        /**ASSERTIVAS*/
        assertEquals(emprestimoMock.getLivros().size(), devolucao.getLivrosDevolvidos().size());
        assertEquals(0, devolucao.getQtdLivrosComUser());
        assertEquals(105.0, devolucao.getValorASerPago());

        assertTrue( devolucao.getDataDevolucao()
                .isEqual(emprestimoMock.getDataPrevista()) );
        System.out.println("TESTE 9 SUCCESS");

    }

    @Test
    @DisplayName("Testa uma devolução 1(um) dia após a data prevista")
    public void devolucao_1_DiaApos() throws ServiceException {
        /**CENÁRIO*/
        Usuario userMock = gerarUser("Marcio", "2010SI201457");
        Emprestimo emprestimoMock = gerarEmprestimo(userMock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_10);
        emprestimoMock.setDataPrevista();
        emprestimoMock.setDataDevolucao(DEZ_OUT_18);
        emprestimoMock.setLivros( getLivros(true, true) );
        emprestimoMock.getUser().setNumeroDeEmprestimos(emprestimoMock.getLivros().size());


        /**AÇÃO*/
        DevolucaoResponse devolucao = emprestimoService.registrarDevolucao(emprestimoMock);


        /**ASSERTIVAS*/
        assertEquals(emprestimoMock.getLivros().size(), devolucao.getLivrosDevolvidos().size());
        assertEquals(0, devolucao.getQtdLivrosComUser());
        Assertions.assertThat(devolucao.getValorASerPago()>75);

        assertTrue( devolucao.getDataDevolucao()
                .isAfter(emprestimoMock.getDataPrevista()) );
        System.out.println("TESTE 10 SUCCESS");

    }

    @Test
    @DisplayName("Testa uma devolução 30 dias após a data prevista")
    public void devolucao_30_DiaApos() throws ServiceException {
        /**CENÁRIO*/
        Usuario userMock = gerarUser("Marcio", "2010SI201457");
        Emprestimo emprestimoMock = gerarEmprestimo(userMock);
        emprestimoMock.setDataEmprestimo(DEZ_OUT_10);
        emprestimoMock.setDataPrevista();
        emprestimoMock.setDataDevolucao(emprestimoMock.getDataPrevista().plusDays(30));
        emprestimoMock.setLivros( getLivros(true, true) );
        emprestimoMock.getUser().setNumeroDeEmprestimos(emprestimoMock.getLivros().size());


        /**AÇÃO*/
        DevolucaoResponse devolucao = emprestimoService.registrarDevolucao(emprestimoMock);


        /**ASSERTIVAS*/
        assertEquals(emprestimoMock.getLivros().size(), devolucao.getLivrosDevolvidos().size());
        assertEquals(0, devolucao.getQtdLivrosComUser());
        Assertions.assertThat(devolucao.getValorASerPago()>120.00);

        assertTrue( devolucao.getDataDevolucao()
                .isAfter(emprestimoMock.getDataPrevista()) );
        System.out.println("TESTE 11 SUCCESS");

    }


    private Usuario gerarUser(String name, String matricula){
        Usuario user = new Usuario(name, matricula);

        return user;
    }

    private Emprestimo gerarEmprestimo(Usuario user){

        return new Emprestimo(user);
    }

    private List<Livro> getLivros(boolean reservado, boolean emprestado){
        List<Livro> livros;

        Livro livro1 = gerarLivro(LIVRO1, reservado, emprestado);
        Livro livro2 = gerarLivro(LIVRO2, reservado, emprestado);
        Livro livro3 = gerarLivro(LIVRO3, reservado, emprestado);

        livros = new LinkedList<Livro>(Arrays.asList(livro1, livro2, livro3));

        return livros;
    }

    private Livro gerarLivro(String name, boolean reservado, boolean emprestado){
        Livro livroMock = new Livro(name, emprestado, reservado);

        return livroMock;
    }
}
