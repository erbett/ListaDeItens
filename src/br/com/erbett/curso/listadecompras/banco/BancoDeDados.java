/* 
 * Copyright 2016 Erbett H. R. Oliveira, Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 		
 * 		1. Redistributions of source code must retain the above copyright notice, this list of
 * 			conditions and the following disclaimer.
 * 
 * 		2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 		   of conditions and the following disclaimer in the documentation and/or other materials
 *         provided with the distribution.
 *         
 * THIS SOFTWARE IS PROVIDED BY ERBETT HINTON RIBEIRO OLIVEIRA, INC. ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, AND LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ERBETT HINTON RIBEIRO OLIVEIRA, INC. OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package br.com.erbett.curso.listadecompras.banco;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import br.com.erbett.curso.listadecompras.obj.ItemLista;

public class BancoDeDados extends SQLiteOpenHelper implements BaseColumns {

	private static final String DATABASE_NAME = "lista_compras_db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_ITENS = "itens_lista";

	private static final String NOME_PRODUTO = "nome";
	private static final String CATEGORIA_PRODUTO = "categoria";
	private static final String QTDE = "qtde";
	private static final String PRECO_UNITARIO = "preco_unit";
	private static final String PRECO_TOTAL = "preco_total";
	
	private SQLiteDatabase db;

	public BancoDeDados(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		String CREATE_ITENS_TABLE =
			"CREATE TABLE " + TABLE_ITENS + "("
				+ _ID + " INTEGER PRIMARY KEY,"
				+ NOME_PRODUTO + " VARCHAR,"
				+ CATEGORIA_PRODUTO + " VARCHAR,"
				+ QTDE + " INTEGER,"
				+ PRECO_UNITARIO + " REAL,"
				+ PRECO_TOTAL + " REAL " + ")";

		db.execSQL(CREATE_ITENS_TABLE);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITENS);
        onCreate(db);
	}

	public void saveItem(ItemLista item) {
		ContentValues values = new ContentValues();
		values.put(NOME_PRODUTO, item.getNome());
		values.put(CATEGORIA_PRODUTO, item.getCategoria());
		values.put(QTDE, item.getQtde());
		values.put(PRECO_UNITARIO, item.getPrecoUnit());
		values.put(PRECO_TOTAL, item.getPrecoTotal());

		db.beginTransaction();

		try {
			if (item.getId() > 0L) {
				String whereClause = _ID + " = ?";
				String[] whereArgs = { String.valueOf(item.getId()) };

				db.update(TABLE_ITENS, values, whereClause, whereArgs);
			} else {
				db.insert(TABLE_ITENS, null, values);
			}

			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public void deleteItem(ItemLista item) {
		String whereClause = _ID + " = ?";
		String[] whereArgs = { String.valueOf(item.getId()) };

		db.beginTransaction();

		try {
			db.delete(TABLE_ITENS, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public ArrayList<ItemLista> getAllItens() {
		ArrayList<ItemLista> itens = new ArrayList<ItemLista>();

		String orderBy = _ID + " ASC";
		Cursor cursor = db.query(TABLE_ITENS, null, null, null, null, null, orderBy);
		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					ItemLista itemLista = new ItemLista();
					itemLista.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
					itemLista.setNome(cursor.getString(cursor.getColumnIndex(NOME_PRODUTO)));
					itemLista.setCategoria(cursor.getString(cursor.getColumnIndex(CATEGORIA_PRODUTO)));
					itemLista.setQtde(cursor.getInt(cursor.getColumnIndex(QTDE)));
					itemLista.setPrecoUnit(cursor.getDouble(cursor.getColumnIndex(PRECO_UNITARIO)));
					itemLista.setPrecoTotal(cursor.getDouble(cursor.getColumnIndex(PRECO_TOTAL)));
					
					itens.add(itemLista);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return itens;
	}

}
