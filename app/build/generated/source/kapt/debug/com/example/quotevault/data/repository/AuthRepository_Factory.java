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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  @Override
  public AuthRepository get() {
    return newInstance();
  }

  public static AuthRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AuthRepository newInstance() {
    return new AuthRepository();
  }

  private static final class InstanceHolder {
    private static final AuthRepository_Factory INSTANCE = new AuthRepository_Factory();
  }
}
