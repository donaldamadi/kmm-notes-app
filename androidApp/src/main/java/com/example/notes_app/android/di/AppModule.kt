package com.example.notes_app.android.di

import android.app.Application
import com.example.notes_app.data.local.DatabaseDriverFactory
import com.example.notes_app.data.notes.SqlDelightNotesDataSource
import com.example.notes_app.database.NotesAppDatabase
import com.example.notes_app.domain.notes.NotesDataSource
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).createDriver()
    }

    @Provides
    @Singleton
    fun provideNoteDataSource(driver: SqlDriver): NotesDataSource {
        return SqlDelightNotesDataSource(NotesAppDatabase(driver))
    }

    
}