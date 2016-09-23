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

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.erbett.curso.listadecompras.R;
import br.com.erbett.curso.listadecompras.banco.BancoDeDados;
import br.com.erbett.curso.listadecompras.obj.ItemLista;

public class ItemDetalheActivity extends Activity implements OnClickListener, TextWatcher {

	private ItemLista itemLista;
	
	private EditText nome;
	private Spinner  categoria;
	private EditText quantidade;
	private EditText precoItem;
	private TextView valorTotal;
	
	private BancoDeDados db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail);

		db = new BancoDeDados(this);

		nome 		= (EditText) findViewById(R.id.edit_text_nome_item);
		categoria 	= (Spinner) findViewById(R.id.spinner_categoria_item);
		quantidade	= (EditText) findViewById(R.id.edit_text_quantidade_item);
		precoItem 	= (EditText) findViewById(R.id.edit_text_preco_item);
		valorTotal 	= (TextView) findViewById(R.id.text_view_preco_total);
		
		quantidade.addTextChangedListener(this);
		precoItem.addTextChangedListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			itemLista = (ItemLista) extras.getSerializable("item_compra");
			if (itemLista != null) {
				String[] categorias = getResources().getStringArray(R.array.categorias);

				int categoriaPosition = 0;
				for (int i = 0; i < categorias.length; i++) {
					if (categorias[i].equals(itemLista.getCategoria())) {
						categoriaPosition = i;
						break;
					}
				}
				
				nome.setText(itemLista.getNome());
				categoria.setSelection(categoriaPosition);
				quantidade.setText(String.valueOf(itemLista.getQtde()));
				precoItem.setText(String.valueOf(itemLista.getPrecoUnit()));
			} 
		}

		Button salvarButton = (Button) findViewById(R.id.button_salvar);
		salvarButton.setOnClickListener(this);

		Button cancelButton = (Button) findViewById(R.id.button_cancelar);
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_salvar:
				
				if (itemLista == null){
					itemLista = new ItemLista();
				}
				
				itemLista.setNome(nome.getText().toString().toUpperCase(Locale.getDefault()));
				itemLista.setCategoria(categoria.getSelectedItem().toString());
				
				if (quantidade.getText().length() > 0){
					itemLista.setQtde(Integer.parseInt(quantidade.getText().toString()));
				}
				
				if (precoItem.getText().length() > 0){
					itemLista.setPrecoUnit(Double.parseDouble(precoItem.getText().toString()));
				}
				
				if (valorTotal.getText().length() > 0){
					itemLista.setPrecoTotal(Double.parseDouble(valorTotal.getText().toString()));
				}

				db.saveItem(itemLista);
				
				finish();
				break;
			case R.id.button_cancelar:
				finish();
				break;
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (quantidade.getText().toString().length() > 0 && precoItem.getText().toString().length() > 0 ){
			int quantidade = Integer.parseInt(this.quantidade.getText().toString());
			double valor = Double.parseDouble(precoItem.getText().toString());
			double valorTotal = valor * quantidade;
			
			this.valorTotal.setText(String.valueOf(valorTotal));
			
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

}
