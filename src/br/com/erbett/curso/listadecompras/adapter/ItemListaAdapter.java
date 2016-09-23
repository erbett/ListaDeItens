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
package br.com.erbett.curso.listadecompras.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.erbett.curso.listadecompras.R;
import br.com.erbett.curso.listadecompras.obj.ItemLista;

public class ItemListaAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ItemLista> itens;
	
	public ItemListaAdapter(Context context, List<ItemLista> itens) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.itens = itens;
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public ItemLista getItem(int position) {
		return itens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return itens.get(position).getId();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		View view = convertView;

		if (view == null){
			view = inflater.inflate(R.layout.list_item, null);
		}

		ItemLista item = itens.get(position);

		TextView itemNome = (TextView) view.findViewById(R.id.text_view_nome_item_list);
		itemNome.setText(item.getNome());

		TextView itemQtde = (TextView) view.findViewById(R.id.text_view_quantidade);
		itemQtde.setText("Qtd.: " + item.getQtde());
		
		TextView itemTotalPreco = (TextView) view.findViewById(R.id.text_view_total_pagar);
		itemTotalPreco.setText("Total: " + item.getPrecoTotal());

		return view;
	}
	
	public void refreshList(List<ItemLista> itens) {
		this.itens = itens;
		notifyDataSetChanged();
	}

	
}
