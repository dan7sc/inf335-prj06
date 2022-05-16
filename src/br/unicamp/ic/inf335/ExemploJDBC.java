package br.unicamp.ic.inf335;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ExemploJDBC {
	/***
	 * Conecta com o Banco de Dados MySQL
	 * @param usuario - Nome do usuário do Banco
	 * @param senha - Senha do usuário do Banco
	 * @param url - jdbc:mysql:// + nome do servicdor / nome da base
	 * @return Conexão com o banco de dados
	 */
	private static Connection conectar(String usuario, String senha, String url) {
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, usuario, senha);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Erro de conexão: " + e);
			e.printStackTrace();
		}

		return conn;

	}

	/***
	 * Lista todos os produtos da loja
	 * @param conn - Conexão com o Base
	 */
	public void listaProdutos(Connection conn) {
		// Lista statement SQL
		PreparedStatement stat;

		try {
			stat = (PreparedStatement) conn.prepareStatement("select * from Produto;");
			// Executa select e coloca resultados em uma variável java em memória
			ResultSet rs = stat.executeQuery();

			// Pega resultado e imprime os Campos
			while(rs.next()) {
				String idProduto = rs.getString("idProduto");
				String nome = rs.getString("nome");
				String descricao = rs.getString("descricao");
				String valor = rs.getString("valor");
				String estado = rs.getString("estado");
				System.out.println(idProduto + " -- " + nome + " -- " + descricao + " -- " + valor + " -- " + estado);
			}
		}
		catch (Throwable e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}

	}

	/***
	 * Insere um novo produto na tabela Produto
	 * @param conn - Conexão com o BD
	 * @param idProduto
	 * @param nome
	 * @param descricao
	 * @param valor
	 * @param estado
	 */
	public void insereProduto(Connection conn, String idProduto, String nome, String descricao, String valor, String estado) {
		// Cria statement SQL
		Statement stmt;

		try {
			stmt = (Statement) conn.createStatement();
			String insere = "insert into Produto VALUES ('"
					+ idProduto + "','"
					+ nome + "','"
					+ descricao + "','"
					+ valor + "','"
					+ estado + "');";

			stmt.executeUpdate(insere);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Altera valor produto
	 * @param conn - Conexão com o BD
	 * @param idProduto - Id do produto a ser alterado
	 * @param valor
	 */
	public void alteraValorProduto(Connection conn, String idProduto, String valor) {
		// Cria statement SQL
		Statement stmt;

		try {
			stmt = (Statement) conn.createStatement();
			String atualiza = "update Produto set valor = '"
					+ valor + "' where idProduto = '"
					+ idProduto + "';";

			stmt.executeUpdate(atualiza);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Apaga um produto
	 * @param conn - Conexão com o BD
	 * @param idProduto - Id do produto a ser apagado
	 */
	public void apagaProduto(Connection conn, String idProduto) {
		// Cria statement SQL
		Statement stmt;

		try {
			stmt = (Statement) conn.createStatement();
			String apaga = "delete from Produto where idProduto = '"
					+ idProduto + "';";

			stmt.executeUpdate(apaga);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExemploJDBC loja = new ExemploJDBC();
		Connection conn = conectar("root", "admin", "jdbc:mysql://172.17.0.2:3306/loja");

		if(conn != null) {
			System.out.println("Lista Original de Produtos");
			// Lista os produtos da Loja
			loja.listaProdutos(conn);

			// Insere novo produto
			loja.insereProduto(conn, "7", "Prod7", "Bla Bla", "500.0", "Bla Bla");
			System.out.println("Lista com Novo Produto");
			// Lista os produtos da Loja
			loja.listaProdutos(conn);

			// Altera valor do produto
			loja.alteraValorProduto(conn, "7", "400.0");
			System.out.println("Lista com Valor do Produto Alterado");
			// Lista com produto alterado
			loja.listaProdutos(conn);

			System.out.println("Apaga Produto Número 7");
			// Apaga produto
			loja.apagaProduto(conn, "7");
			System.out.println("Volta a Lista Original de Produtos");
			// Lista os produtos da Loja
			loja.listaProdutos(conn);

			// Fecha conexão com banco de dados
			try {
				conn.close();
			}
			catch (SQLException e) {
				System.out.println("Erro ao fechar conexâo: " + e);
				e.printStackTrace();
			}

		}
	}
}
