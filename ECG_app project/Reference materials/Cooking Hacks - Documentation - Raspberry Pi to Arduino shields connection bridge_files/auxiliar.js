/***********************************************************
*   Auxiliar javascript functions                          *
*   NOTE: All code here must use $j instead of $ for       *
*   jquery to avoid javascript conflict                    *
***********************************************************/

$j(document).ready(function() {

// Add show_hide link to all code blocks
$j('div.codigo').prepend('<a href="#">Show Code</a>').
find('pre').addClass('codigo_plegado');

//Handle code a
$j('div.codigo a').click(function() {
	if ($j(this).parent('div').find('pre').hasClass('codigo_plegado')){
	$j(this).parent('div').find('pre').
	removeClass('codigo_plegado').addClass('codigo_desplegado');
	$j(this).html('Hide Code');
}
else {
	$j(this).parent('div').find('pre').
	addClass('codigo_plegado').removeClass('codigo_desplegado');
	$j(this).html('Show Code');
}
return false;
});
 
//Adjust size images shor-description
setTimeout(function() {
	$j('div.product-shop div.short-description img').each(function() {
		if ($j(this).width() > 330) $j(this).width(330);
	});
},500);

//Adjust item list height
$j('ul.products-grid').each(function() {  
	var height = $j(this).height()-98;
	$j(this).find('li.item').height(height);
});

 //Popup payment method && Notify time processing order
  if ($j('#opc-payment').length) {

    $j('#opc-review').append('<button style="display:none" type="submit"></button>');

    var message = ($j('#opc-payment div.step-title h2').html() == "Payment Information")? "We are improving the checkout method: \\n\\n If you have problems in the checkout process please contact us: \\n\\n \\t - Phone: +34 976 54 74 92 \\n\\t - E-mail: info@cooking-hacks.com \\n\\n Thanks" : "Estamos mejorando el sistema de pedidos: \\n\\nSi experimenta problemas al realizar el pedido por favor pongase en contacto con nosotros: \\n\\n \\t - Teléfono:  +34 976 54 74 92 \\n\\t - E-mail: info@cooking-hacks.com \\n\\n Gracias";

    var alert = ($j('#opc-payment div.step-title h2').html() == "Payment Information")? "It may take several minutes" : "Puede durar varios minutos";

    $j('#opc-payment #payment-buttons-container button').attr('onclick',"if ($j('input[name=\"payment[method]\"]:checked').val() != undefined) { alert(\""+message+"\"); }setTimeout(function() {$j('#review-please-wait').append('("+alert+")')},2500);payment.save();");
  }
});
