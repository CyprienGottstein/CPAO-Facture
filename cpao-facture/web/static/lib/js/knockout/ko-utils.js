/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Code Stolen from : http://stackoverflow.com/questions/14683953/show-twitter-bootstrap-modal-dialog-automatically-with-knockout
ko.bindingHandlers.showModal = {
    init: function (element, valueAccessor) {},
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        if (ko.utils.unwrapObservable(value)) {
            $(element).modal('show');
                // this is to focus input field inside dialog
                $("input", element).focus();
            }
            else {
                $(element).modal('hide');
            }
        }
    };
