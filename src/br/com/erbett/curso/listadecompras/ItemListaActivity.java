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
package br.com.erbett.curso.listadecompras;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import br.com.erbett.curso.listadecompras.R;
import br.com.erbett.curso.listadecompras.adapter.ItemListaAdapter;
import br.com.erbett.curso.listadecompras.banco.BancoDeDados;
import br.com.erbett.curso.listadecompras.obj.ItemLista;

public class ItemListaActivity extends ListActivity implements OnItemLongClickListener {

	private BancoDeDados db;
	private List<ItemLista> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new BancoDeDados(this);
		adapter = db.getAllItens();
		
		setListAdapter(new ItemListaAdapter(this, adapter));
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		getListView().setOnItemLongClickListener(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		adapter = db.getAllItens();
		
		((ItemListaAdapter) getListAdapter()).refreshList(adapter);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		
			case R.id.item_menu_adicionar_novo:
				startActivity(new Intent(this, ItemDetalheActivity.class));
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ItemLista item = ((ItemListaAdapter) getListAdapter()).getItem(position);
		Intent intent = new Intent(this, ItemDetalheActivity.class);
		intent.putExtra("item_compra", item);
		
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
		final ItemListaAdapter adapter = (ItemListaAdapter) getListAdapter();
		
		final ItemLista item = adapter.getItem(position);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Remover Item");
		alert.setMessage("Deseja remover este Item ?");

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				db.deleteItem(item);
				
				ItemListaActivity.this.adapter = db.getAllItens();
				
				adapter.refreshList(ItemListaActivity.this.adapter);
				dialog.dismiss();
			}
		});

		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});

		alert.show();

		return false;
	}

}
