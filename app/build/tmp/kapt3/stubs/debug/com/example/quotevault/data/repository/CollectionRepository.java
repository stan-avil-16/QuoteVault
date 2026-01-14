package com.example.quotevault.data.repository;

import com.example.quotevault.data.model.Collection;
import com.example.quotevault.data.model.CollectionQuote;
import com.example.quotevault.data.model.Quote;
import io.github.jan.supabase.postgrest.query.Columns;
import kotlinx.coroutines.Dispatchers;
import kotlinx.serialization.Serializable;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0007J&\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00132\u0006\u0010\u0004\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0007J&\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u001a"}, d2 = {"Lcom/example/quotevault/data/repository/CollectionRepository;", "", "()V", "addQuoteToCollection", "collectionId", "", "quoteId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createCollection", "", "name", "description", "createCustomQuote", "text", "author", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCustomQuote", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCollections", "", "Lcom/example/quotevault/data/model/Collection;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getQuotesInCollection", "Lcom/example/quotevault/data/model/Quote;", "removeQuoteFromCollection", "updateCustomQuote", "app_debug"})
public final class CollectionRepository {
    
    @javax.inject.Inject()
    public CollectionRepository() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCollections(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.quotevault.data.model.Collection>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addQuoteToCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String collectionId, @org.jetbrains.annotations.NotNull()
    java.lang.String quoteId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removeQuoteFromCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String collectionId, @org.jetbrains.annotations.NotNull()
    java.lang.String quoteId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getQuotesInCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String collectionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.quotevault.data.model.Quote>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createCustomQuote(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String author, @org.jetbrains.annotations.NotNull()
    java.lang.String collectionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCustomQuote(@org.jetbrains.annotations.NotNull()
    java.lang.String quoteId, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String author, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteCustomQuote(@org.jetbrains.annotations.NotNull()
    java.lang.String quoteId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
}