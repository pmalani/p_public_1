package m

import com.google.common.cache.CacheBuilder;

class Security {
    private String name;
    
    Security(String s) {
        name = s;        
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Security [name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Security other = (Security) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    public static Security make(String s) {
        return new Security(s)
    }
    
    public static Security allianz() {
        return make("Allianz")
    }
    
    public static Security marconi() {
        return make("Marconi")
    }
    
    public static Security vodafone() {
        return make("Vodafone")
    }
    
    public static Security aol() {
        return make("AOL")
    }
    
    public static Security microsoft() {
        return make("Microsoft")
    }
}

class Position {
    private Security security
    private def weight
    private def rtn
    
    public Position(Security s, weight, rtn) {
        security = s
        this.weight = weight
        this.rtn = rtn
    }
    
    public Security getSecurity() {
        return security
    }
    
    public def getWeight() {
        return weight
    }
    
    public def getRtn() {
        return rtn
    }

    @Override
    public String toString() {
        return "Position [security=" + security + ", weight=" + weight + ", rtn=" + rtn + "]";
    }
}

class Attribution {
    private Security security
    
    def allocation = 0.0;
    def timing = 0.0;
    
    public Attribution(Security s) {
        security = s
    }
    
    Security getSecurity() {
        return security;
    }

    @Override
    public String toString() {
        return "Attribution [security=" + security + ", allocation=" + allocation + ", timing=" + timing
            + "]";
    }
}

class Universe {
    List<Position> positions = [];
    
    def totalRtn() {
        positions.sum { x ->  x.weight/100 * x.rtn }
    }
    
    void leftShift(Position p) {
        positions << p
    }
     
    static Universe makeSamplePortfolio() {    
        Universe u = new Universe()
        u << new Position(Security.allianz(), 10.2, 15.0)
        u << new Position(Security.marconi(), 5.2, -25.0)
        u << new Position(Security.vodafone(), 15.0, 3.4)
        u << new Position(Security.aol(), 22.0, -5.2)
        u << new Position(Security.microsoft(), 47.6, 6.9)
        return u    
    }
    
    static Universe makeSampleBenchmark() {
        Universe u = new Universe()
        u << new Position(Security.allianz(), 9.3, 15.0)
        u << new Position(Security.marconi(), 0.1, -25.0)
        u << new Position(Security.vodafone(), 20.8, 3.4)
        u << new Position(Security.aol(), 14.7, -5.2)
        u << new Position(Security.microsoft(), 55.1, 7.5)
        return u
    }
}

class UniversePair {
    private Universe portfolio
    private Universe benchmark
    
    private List<Attribution> attributions

    public UniversePair(Universe aPortfolio, Universe aBenchmark) {
        portfolio = aPortfolio
        benchmark = aBenchmark
        attributions = aPortfolio.positions.collect {
            x -> new Attribution(x.security)
        }
    }    
        
    public Universe getPortfolio() {
        return portfolio
    }
    
    public Universe getBenchmark() {
        return benchmark
    }    
    
    public w(int i) {
        portfolio.positions[i].weight
    }
    
    public W(int i) {
        benchmark.positions[i].weight
    }
    
    def r(int i) {
        portfolio.positions[i].rtn / 100.0
    }
    
    def b(int i) {
        benchmark.positions[i].rtn / 100.0
    }
    
    void computeAllocations() {
        def benchmarkTotalRtn = benchmark.totalRtn() / 100.0
        attributions.eachWithIndex { x, i 
            -> x.allocation = (w(i) - W(i)) * ((1 + b(i))/(1+benchmarkTotalRtn) -1)
        }
    }
    
    void computeTiming() {
        
    }

    @Override
    public String toString() {
        return "UniversePair [attributions=" + attributions + "]";
    }    
}

println Universe.makeSamplePortfolio().totalRtn()
println Universe.makeSampleBenchmark().totalRtn()

UniversePair p = new UniversePair(Universe.makeSamplePortfolio(), Universe.makeSampleBenchmark())
p.computeAllocations()
println p