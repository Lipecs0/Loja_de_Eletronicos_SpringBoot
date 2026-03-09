package com.loja.eletronicos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loja.eletronicos.model.Produto;
import com.loja.eletronicos.repository.ProdutoRepository;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final ProdutoRepository produtoRepository;

    public CompraController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // DTO interno simples
    public static class CompraRequest {
        public List<Long> produtosIds;
    }

    @PostMapping
    public ResponseEntity<?> finalizarCompra(@RequestBody CompraRequest compra) {
        if (compra.produtosIds == null || compra.produtosIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Carrinho vazio");
        }

        List<Produto> produtos = produtoRepository.findAllById(compra.produtosIds);

        // Verifica se todos existem
        if (produtos.size() != compra.produtosIds.size()) {
            return ResponseEntity.status(404).body("Alguns produtos não existem");
        }

        // Verifica estoque
        for (Produto p : produtos) {
            if (p.getEstoque() <= 0) {
                return ResponseEntity.status(409).body("Produto sem estoque: " + p.getNome());
            }
        }

        // Reduz estoque
        for (Produto p : produtos) {
            p.setEstoque(p.getEstoque() - 1);
        }

        produtoRepository.saveAll(produtos);

        return ResponseEntity.ok("Compra realizada com sucesso");
    }
}

