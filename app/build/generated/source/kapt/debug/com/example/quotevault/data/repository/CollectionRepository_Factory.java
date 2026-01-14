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
public final class CollectionRepository_Factory implements Factory<CollectionRepository> {
  @Override
  public CollectionRepository get() {
    return newInstance();
  }

  public static CollectionRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CollectionRepository newInstance() {
    return new CollectionRepository();
  }

  private static final class InstanceHolder {
    private static final CollectionRepository_Factory INSTANCE = new CollectionRepository_Factory();
  }
}
