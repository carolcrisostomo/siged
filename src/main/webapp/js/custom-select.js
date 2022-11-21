var divCustomSelectList, i, j, selectElement, divSelected, divSelectItems, divOption, divOptionList, divSelectContent, divSearch, inputSearch, currentFocus;

/* Procurando elementos com a classe 'custom-select': */
divCustomSelectList = document.getElementsByClassName('custom-select');

for (i = 0; i < divCustomSelectList.length; i++) {

	selectElement = divCustomSelectList[i].getElementsByTagName('select')[0];

	/* Cria uma nova DIV que se comportará como um option selecionado: */
	divSelected = document.createElement('DIV');
	divSelected.setAttribute('class', 'select-selected');
	divSelected.innerHTML = selectElement.options[selectElement.selectedIndex].innerHTML;
	divCustomSelectList[i].appendChild(divSelected);
	
	
	divSelectContent = document.createElement('DIV');
	divSelectContent.setAttribute('class', 'select-content select-hide');
	
	divSearch = document.createElement('DIV');
	divSearch.setAttribute('class', 'div-search');
	
	inputSearch = document.createElement('INPUT');
	inputSearch.setAttribute('type', 'text');
	inputSearch.setAttribute('placeholder', 'Digite o título do evento');
	
	inputSearch.addEventListener('click', function(event) {		
		event.stopPropagation();
	});
	
	inputSearch.addEventListener('input', function(e) {
		var i, divOptionList, divSelectItems, listaFiltrada = [];		
		
		if (!this.value) { return false;}		
		
		divOptionList = obterListaDeDivOptionDoSelect(this.parentNode.parentNode.parentNode.getElementsByTagName('select')[0]);	
		
		for (i = 0; i < divOptionList.length; i++) {						
			if (divOptionList[i].innerHTML.toUpperCase().indexOf(this.value.toUpperCase()) > -1 ) {
				divOptionList[i].innerHTML = divOptionList[i].innerHTML.replace(this.value.toUpperCase(), '<b>' + this.value.toUpperCase() + '</b>');
				listaFiltrada.push(divOptionList[i]);			
			}        
		}
		
		divSelectItems = this.parentNode.parentNode.getElementsByClassName('select-items')[0];		
		divSelectItems.innerHTML = [];
		for (i = 0; i < listaFiltrada.length; i++) {
			divSelectItems.appendChild(listaFiltrada[i]);
		}	
	});
	
	inputSearch.addEventListener("keydown", function(e) {
		var i, divOptionList;		
		
		divOptionList = this.parentNode.parentNode.getElementsByClassName('select-items')[0].getElementsByTagName("div");
				
		
		if (e.keyCode == 40) {	//If the arrow DOWN key is pressed
			currentFocus++;
			addActive(divOptionList);
			divOptionList[currentFocus].parentNode.scrollTop = divOptionList[currentFocus].offsetTop;
	    
		} else if (e.keyCode == 38) { //If the arrow UP key is pressed	        
			currentFocus--;
			addActive(divOptionList);
	        
		} else if (e.keyCode == 13) { //If the ENTER key is pressed
			e.preventDefault();
			if (currentFocus > -1) {
	          if (divOptionList) {	        	  
	        	  divOptionList[currentFocus].click();
	          }
	        }
		}
		
	});	
	
	function addActive(divOptionList) {

		if (!divOptionList) return false;
		
		removeActive(divOptionList);

		if (currentFocus >= divOptionList.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (divOptionList.length - 1);

		divOptionList[currentFocus].classList.add("autocomplete-active");
	}	
	function removeActive(divOptionList) {
    
		for (var i = 0; i < divOptionList.length; i++) {
			divOptionList[i].classList.remove("autocomplete-active");
		}
	}
	
	
	divSearch.appendChild(inputSearch);	
	divSelectContent.appendChild(divSearch);	
		
	/* Cria uma nova DIV que irá conter a lista de options: */
	divSelectItems = document.createElement('DIV');
	divSelectItems.setAttribute('class', 'select-items');
	
	divOptionList = obterListaDeDivOptionDoSelect(selectElement);	

	for (j = 0; j < divOptionList.length; j++) {
		divSelectItems.appendChild(divOptionList[j]);
	}	
	
	divSelectContent.appendChild(divSelectItems);
		
	
	divCustomSelectList[i].appendChild(divSelectContent);

	
	divSelected.addEventListener('click', function(event) {
		
		var i, divOptionList, divSelectItems;
		
		event.stopPropagation();
		closeAllSelect(this);
		this.nextSibling.classList.toggle('select-hide');
		this.classList.toggle('select-arrow-active');		
		
		if ( (" " + this.className + " ").replace(/[\n\t]/g, " ").indexOf(" select-hide ") == -1 ) {			
			
			this.parentNode.getElementsByTagName('input')[0].focus();
			
			divOptionList = obterListaDeDivOptionDoSelect(this.parentNode.getElementsByTagName('select')[0])			
			divSelectItems = this.parentNode.getElementsByClassName('select-items')[0];		
			divSelectItems.innerHTML = [];
			for (i = 0; i < divOptionList.length; i++) {
				divSelectItems.appendChild(divOptionList[i]);
			}
		}
	});

}

function closeAllSelect(element) {
	/* Função que fechará todas as divs select na página, exceto a div select atual: */
	var i, divOptionsList, divOptionSelectedList, divsOptionsASeremFechadas = [];
	
	divOptionSelectedList = document.getElementsByClassName('select-selected');
		
	for (i = 0; i < divOptionSelectedList.length; i++) {
		if (element == divOptionSelectedList[i]) {
			divsOptionsASeremFechadas.push(i)
		} else {
			divOptionSelectedList[i].classList.remove('select-arrow-active');
		}
	}
	
	divOptionsList = document.getElementsByClassName('select-items');
	
	for (i = 0; i < divOptionsList.length; i++) {	
		
		if (divsOptionsASeremFechadas.indexOf(i)) {
			
			divOptionsList[i].parentNode.getElementsByTagName('input')[0].value = null;
			divOptionsList[i].parentNode.classList.add('select-hide');
		}
	}
	
	currentFocus = -1;
}

/* Se o usuário clicar em qualquer local fora da div select, todas as divs select serão fechadas: */
document.addEventListener('click', closeAllSelect);


function obterListaDeDivOptionDoSelect(selectElement){
	
	var i, divOption, divSelectItems = [];
	
	for (i = 0; i < selectElement.length; i++) {
		
		/* Para cada option do select original, cria uma nova DIV que se comportará como um option: */
		divOption = document.createElement('DIV');
		divOption.innerHTML = selectElement.options[i].innerHTML;
		
		if(selectElement.options[i].selected) {
			divOption.setAttribute('class', 'same-as-selected');
		};
		
		divOption.addEventListener('click', function() {
			/* Quando uma div option for clicada, atualiza o select original e o option selecionado: */
			var i, j, selectElement, divCustomSelect, divOptionList;
			
			selectElement = this.parentNode.parentNode.parentNode.getElementsByTagName('select')[0];
			divCustomSelect = this.parentNode.parentNode.previousSibling;
			
			for (i = 0; i < selectElement.length; i++) {
				
				if (selectElement.options[i].innerHTML == this.innerHTML.replace('<b>', '').replace('</b>', '')) {
					
					selectElement.selectedIndex = i;
					divCustomSelect.innerHTML = this.innerHTML;
					divOptionList = this.parentNode.parentNode.getElementsByClassName('same-as-selected');
					
					for (j = 0; j < divOptionList.length; j++) {
						divOptionList[j].removeAttribute('class');
					}
					
					this.setAttribute('class', 'same-as-selected');
					break;
				}
			}
			
			divCustomSelect.click();
		});

		divSelectItems.push(divOption);
	}
	
	return divSelectItems;
	
}
