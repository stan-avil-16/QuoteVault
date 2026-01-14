package com.example.quotevault.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class QuoteRepository_Factory implements Factory<QuoteRepository> {
  @Override
  public QuoteRepository get() {
    return newInstance();
  }

  public static QuoteRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static QuoteRepository newInstance() {
    return new QuoteRepository();
  }

  private static final class InstanceHolder {
    private static final QuoteRepository_Factory INSTANCE = new QuoteRepository_Factory();
  }
}
