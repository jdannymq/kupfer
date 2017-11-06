 
jQuery(document).ready(function(){ 
	jQuery('#modalCargando').overlay({ mask: { color: '#D0D0D0', loadSpeed: 200, opacity: 0.5},
		closeOnClick: false, closeOnEsc: false,top:'40%'}); 
});

function isNumberKey(evt) {
     var charCode = (evt.which) ? evt.which : event.keyCode;
     if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

     return true;
}
 
 function isRutValidCharacter(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    console.log(charCode);
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    	if(charCode == 75 || charCode == 107) {
    		console.log('es k');
    		return true;
    	}
    	if(charCode == 45) {
    		console.log('es -');
    		return true;
    	}
    	console.log(false);
       return false;
 	}
    console.log(true);
    return true;
}
function abrirModal() {
	jQuery('#modalCargando').overlay().load();
}
function cerrarModal() {
	jQuery('#modalCargando').overlay().close();
}



 