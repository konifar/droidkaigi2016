package com.konifar.confsched.dao;

import android.support.annotation.NonNull;

import com.github.gfx.android.orma.TransactionTask;
import com.konifar.confsched.model.MySession;
import com.konifar.confsched.model.MySession_Relation;
import com.konifar.confsched.model.OrmaDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class MySessionDao {

    OrmaDatabase orma;

    @Inject
    public MySessionDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private MySession_Relation relation() {
        return orma.relationOfMySession();
    }

    public void delete(@NonNull int sessionId) {
        orma.transactionAsync(new TransactionTask() {
            @Override
            public void execute() throws Exception {
                relation().deleter().sessionIdEq(sessionId).execute();
            }
        });
    }

    public void insert(@NonNull int sessionId) {
        if (relation().selector().sessionIdEq(sessionId).count() == 0) {
            orma.transactionAsync(new TransactionTask() {
                @Override
                public void execute() throws Exception {
                    relation().deleter().sessionIdEq(sessionId).execute();
                }
            });
        }
    }

    public List<MySession> findAll() {
        return Observable.from(relation().selector().toList())
                .toList().toBlocking().single();
    }

}
