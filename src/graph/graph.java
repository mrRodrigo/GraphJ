package graph;



import java.util.ArrayList;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


import java.util.HashMap;
import java.util.Map;

public class graph {
	public Vertice auxx;
	public int cont = 0;
	

	public class Vertice {
		String nome;
		boolean marcado;
		List<Aresta> adj;
		List<Aresta> dependentes;
		long valorTotal;
		long peso;

		Vertice(String nome, long peso) {
			this.nome = nome;
			this.marcado = false;
			this.adj = new ArrayList<Aresta>();
			this.dependentes= new ArrayList<Aresta>();
			this.peso = peso;
			valorTotal=0;
		}

		void addAdj(Aresta e) {
			adj.add(e);
		}
		void addDependente(Vertice achavertice, Vertice achavertice2, long quantidade) {
			Aresta e = new Aresta (achavertice, achavertice2, quantidade);
			dependentes.add(e);
		}

		

	}

	public class Aresta {
		Vertice origem;
		Vertice destino;
		long uso; //quantidade de vezes que origem usa destino

		Aresta(Vertice origem, Vertice destino, long uso) {
			this.origem = origem;
			this.destino = destino;
			this.uso = uso;
		}
	}

	
	
	
	List<Vertice> vertices;
	List<Aresta> arestas;
	public int gastoTotal;
	public graph() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
		gastoTotal = 0;
	}

	Vertice addVertice(String nome,long peso) {
		Vertice v = new Vertice(nome,peso);
		vertices.add(v);
		return v;
	}

	Aresta addAresta(Vertice origem, Vertice destino, long uso) {
		Aresta e = new Aresta(origem, destino, uso);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	void marca(Vertice c) {
		c.marcado = true;
	}

	
	//retorna a posi��o do vertice
	Vertice achavertice(String nome) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).nome.equals(nome)) {
				return vertices.get(i);
			}

		}
		return null;
	}
/*
	void calculaDistancias(graph g, String inicio) {

		Queue<Vertice> q = new LinkedList<>();
		int aux = g.achavertice(inicio);

		Map<String, Integer> distancias = new HashMap<String, Integer>();
		Set<String> chaves = distancias.keySet();

		for (int i = 0; i < g.vertices.size(); i++) {
			distancias.put(g.vertices.get(i).nome, -1);

		}

		distancias.put(inicio, 0);
		g.vertices.get(aux).nivel = 0;

		q.add(g.vertices.get(aux));

		while (!q.isEmpty()) {
			Vertice atual = q.poll();

			for (int i = 0; i < atual.adj.size(); i++) {

				if (distancias.get(atual.adj.get(i).destino.nome) == -1) {

					distancias.put(atual.adj.get(i).destino.nome, distancias.get(atual.nome) + 1);
					atual.adj.get(i).destino.nivel = distancias.get(atual.nome) + 1;
					q.add(atual.adj.get(i).destino);

				}
			}

		}

	}

	void achaMenorCaminho(graph g, String inicio, String fim) {

		Deque<Vertice> q = new LinkedList<>();

		int aux = g.achavertice(fim);

		q.push(g.vertices.get(aux));

		String caminho = "";

		while (!q.isEmpty()) {

			Vertice atual = q.pop();

			for (int i = 0; i < atual.adj.size(); i++) {

				if (atual.adj.get(i).destino.nivel == atual.nivel - 1) {
					System.out.println(atual.adj.get(i).conta + " " + atual.adj.get(i).origem.nome + " "
							+ atual.adj.get(i).destino.nome);

					q.push(atual.adj.get(i).destino);
				}
				if (atual.nome.equals(inicio)) {
					break;
				}
			}

		}

	}

	void desmarcaTodos(graph g, String inicio) {

		Queue<Vertice> q = new LinkedList<>();

		int aux = g.achavertice(inicio);

		q.add(g.vertices.get(aux));
		g.vertices.get(aux).marcado = true;

		while (!q.isEmpty()) {

			for (int i = 0; i < q.peek().adj.size(); i++) {

				if (q.peek().adj.get(i).destino.marcado) {

					q.peek().adj.get(i).destino.marcado = false;
					q.add(q.peek().adj.get(i).destino);

				}
			}
			q.poll();
		}

	}
*/
	void percorreLargura(graph g, String inicio) {

		Queue<Vertice> q = new LinkedList<>();

		Vertice aux = g.achavertice(inicio);

		q.add(g.achavertice(inicio));
		g.achavertice(inicio).marcado = true;

		while (!q.isEmpty()) {

			for (int i = 0; i < q.peek().adj.size(); i++) {

				if (!q.peek().adj.get(i).destino.marcado) {
					System.out.println(q.peek().adj.get(i).destino.nome);
					q.peek().adj.get(i).destino.marcado = true;
					q.add(q.peek().adj.get(i).destino);

				} else if (q.contains(q.peek().adj.get(i).destino)) {
					System.out.println(q.peek().adj.get(i).destino.nome);
				}
			}
			q.poll();
		}

	}

	void percorreProfundidade(graph g, String nodo) {

		g.achavertice(nodo).marcado = true;

		System.out.println(g.achavertice(nodo).nome);

		for (int i = 0; i < g.achavertice(nodo).adj.size(); i++) {

			if (g.achavertice(nodo).adj.get(i).destino.marcado == false) {
				percorreProfundidade(g, g.achavertice(nodo).adj.get(i).destino.nome);
			}
		}

	}
	
	void calculaCustos(graph g, Vertice inicio) {

		long valorNodoAtual = inicio.peso;
		inicio.valorTotal += valorNodoAtual;

		for(Aresta a : inicio.dependentes ) {
			a.destino.valorTotal += (valorNodoAtual * a.uso) ;
		}
		
		inicio.marcado = true;
		
		for(Aresta a : inicio.dependentes ) {
			if (temFilhosNaoMarcados(a.destino)) {
				calculaCustos(g, a.destino);
			}
		}
		
	}
	
	boolean temFilhosNaoMarcados(Vertice v) {
		for ( Aresta a : v.adj) {
			if (a.destino.marcado == false) {
				return true;
			}
		}
		return false;
	}
	public Vertice prucuraUltimo(graph g) {
	
		for (Vertice v : vertices ) {
			if (v.adj.size() == 0) {
				return v;
			}
		}
		return null;
	}

	
	public String toString() {
		String r = "";
		for (Vertice u : vertices) {
			r += u.nome + " -> ";
			for (Aresta e : u.adj) {
				Vertice v = e.destino;
				r += v.nome + " vezes de uso: " + e.uso + "\n";
			}
			r += "\n";
		}
		return r;
	}
	public String imprimePesos() {
		String r = "";
		for (Vertice u : vertices) {
			r += u.nome + " -> ";
			r += u.valorTotal + " -> ";
			for (Aresta a : u.dependentes) {
				r += a.destino.nome + ", ";
			}
			r += "\n";
		}
		System.out.print(r);
		return r;
	}

}