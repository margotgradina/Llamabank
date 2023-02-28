// $(document).ready(function() {
//     $('#example').DataTable( {
//         "language": {
//             "lengthMenu": "Display _MENU_ records per page",
//             "zeroRecords": "Nothing found - sorry",
//             "info": "Showing page _PAGE_ of _PAGES_",
//             "infoEmpty": "No records available",
//             "infoFiltered": "(filtered from _MAX_ total records)"
//         }
//     } );
// } );

$(document).ready(function() {

    var t = $('#example').DataTable( {
               "lengthMenu": [[10,15, 25, 50,100, -1], [10, 15,25, 50,100, "All"]],"iDisplayLength": 10,"pagingType": "full_numbers",
    "columnDefs": [ {
               "searchable": false,
               "orderable": false,
               "targets": 0
           } ],
           "order": [[ 1, 'asc' ]]
       } );
    
       t.on( 'order.dt search.dt', function () {
           t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
               cell.innerHTML = i+1;
           } );
       } ).draw();
   } );
   
   var prm = Sys.WebForms.PageRequestManager.getInstance();
   
   prm.add_endRequest(function() {
    var t = $('#example').DataTable( {
               "lengthMenu": [[10,15, 25, 50,100, -1], [10, 15,25, 50,100, "All"]],"iDisplayLength": 15,"language":{"url":"https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Turkish.json"},"pagingType": "full_numbers",
    "columnDefs": [ {
               "searchable": false,
               "orderable": false,
               "targets": 0
           } ],
           "order": [[ 1, 'asc' ]]
       } );
    
       t.on( 'order.dt search.dt', function () {
           t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
               cell.innerHTML = i+1;
           } );
       } ).draw();
   });