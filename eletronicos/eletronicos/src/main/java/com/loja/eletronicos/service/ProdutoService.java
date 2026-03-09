package com.loja.eletronicos.service;

import com.loja.eletronicos.model.Produto;
import com.loja.eletronicos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto novoProduto) {
        return repository.findById(id).map(produto -> {
            produto.setNome(novoProduto.getNome());
            produto.setDescricao(novoProduto.getDescricao());
            produto.setPreco(novoProduto.getPreco());
            produto.setEstoque(novoProduto.getEstoque());
            produto.setCategoria(novoProduto.getCategoria());
            produto.setImagemUrl(novoProduto.getImagemUrl());
            return repository.save(produto);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
