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
public final class FavoriteRepository_Factory implements Factory<FavoriteRepository> {
  @Override
  public FavoriteRepository get() {
    return newInstance();
  }

  public static FavoriteRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FavoriteRepository newInstance() {
    return new FavoriteRepository();
  }

  private static final class InstanceHolder {
    private static final FavoriteRepository_Factory INSTANCE = new FavoriteRepository_Factory();
  }
}
