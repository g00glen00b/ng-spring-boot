describe('The application controller', function() {
  var $scope, factory, saveCallback, queryCallback;
  
  var ITEM1 = {
    id: 1,
    description: 'My first item',
    checked: false,
    $remove: function(callback) {
      callback();
    }
  }, ITEM2 = {
    id: 2,
    description: 'My second item',
    checked: true,
    $remove: function(callback) {
      callback();
    }
  }, DESCRIPTION = "A description";
  
  beforeEach(module('myApp.controllers'));
  beforeEach(inject(function($controller, $rootScope) {
    factory = function() { };
    factory.prototype.$save = function(cb) {
      saveCallback = cb;
    };
    factory.query = function(cb) {
      queryCallback = cb;
    };
    
    $scope = $rootScope.$new();
    $controller('AppController', {
      '$scope': $scope,
      'Item': factory
    });
  }));
  
  it('should have correct items', function() {
    // When item1 and item2 are returned from service
    queryCallback([ITEM1, ITEM2]);
    
    expect($scope.items).toContain(ITEM1);
    expect($scope.items).toContain(ITEM2);
    expect($scope.items.length).toBe(2);
  });
  
  it('should clear the textfield when adding a new item', function() {
    // When the new description is entered inside the textbox
    $scope.newItem = DESCRIPTION;
    $scope.addItem(DESCRIPTION);
    
    expect($scope.newItem).toBe("");
  });
  
  it('should save the item when adding a new item', function() {
    // When saving an item returns the persisted result
    $scope.items = [];
    $scope.addItem(DESCRIPTION);
    saveCallback(ITEM1);
    
    expect($scope.items).toContain(ITEM1);
    expect($scope.items.length).toBe(1);
  });
  
  it('should update the item when changing it', function() {
    // When an item is changed
    var changedItem = jasmine.createSpyObj('Item', ['$update']);
    $scope.updateItem(changedItem);
    
    expect(changedItem.$update).toHaveBeenCalled();
  });
  
  it('should remove the item from the list when it\'s deleted', function() {
    // When there are two items and ITEM1 is removed
    $scope.items = [ITEM1, ITEM2];
    $scope.deleteItem(ITEM1);
    
    expect($scope.items.length).toBe(1);
    expect($scope.items).toContain(ITEM2);
    expect($scope.items).not.toContain(ITEM1);
  });
});