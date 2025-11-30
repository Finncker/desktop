package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Transaction;

public class TransactionRepository extends AbstractRepository<Transaction> {

    public TransactionRepository() {
        super("transactions.dat");
    }

    @Override
    protected String getId(Transaction entity) {
        return entity.getId().toString();
    }

    @Override
    protected boolean matchId(Transaction entity, String id) {
        return entity.getId().toString().equals(id);
    }
    
}
