# `Atlas Prisons`

# Mines
# Enchants

__EXAMPLES__

```java
// When using lombok you can use @Getter / @AllArgsConstructor

public final class TestData implements AtlasEnchantData { 

    private final TestEnchant parent;
    private final double cost;
    private final String information;

    public TestData(TestEnchant parent, String information, double cost) {
        this.parent = parent;
        this.information = information;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
    
    public TestEnchant getParent() {
        return parent;
    }
    
    public String getInformation() {
        return information;
    }
}

/*
This is the test enchant class, here you'll implement the AtlasEnchant interface.
 You then will use the TestData class you made as the Enchants data.
*/

public final class TestEnchant implements AtlasEnchant<TestData> { 

    @Override
    public String name() {
       return "Test Enchant";
   }

   @Override
   public TestData createEnchantData(ConfigurationSection section) {
       return new TestData(this, 
            "THE TEST ENCHANT!!!", 
            AtlasEnchantCost.createCost(1, section).getCost());
   }
}
```

# Robots
# Gangs


