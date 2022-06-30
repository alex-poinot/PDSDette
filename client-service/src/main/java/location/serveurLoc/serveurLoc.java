package location.serveurLoc;

import static client.Client.getSend;
import static client.Client.map;

public class serveurLoc {
    
    public String[] initPlan(String bat, String etage) {
        map.get("initPlanLocation").put("rl_building", bat);
        map.get("initPlanLocation").put("rl_floor", etage);
        String responses = getSend("initPlanLocation");
        System.out.println(responses);
        String[] tab = responses.split("-");
        return tab;
    }

    public int getPlace(String bat) {
        map.get("getPlace").put("rl_building", bat);
        String responses = getSend("getPlace");
        return Integer.parseInt(responses);
    }

    public String[] listBat() {
        String responses = getSend("getListBuilding");
        String[] listeBat = responses.split("-");
        return listeBat;
    }

    public String[] listEtage(String bat) {
        map.get("getListFloor").put("rl_building", bat);
        String responses = getSend("getListFloor");
        String[] tabEtage = responses.split("-");
        return tabEtage;
    }

    public String getNomBat(int i) {
        map.get("getNameBuilding").put("rl_building", (i+1)+"");
        String responses = getSend("getNameBuilding");
        return responses;
    }

    public void replaceEtage(String bat, String etage, String[] tab) {
        String str = "";
        for(int i = 0; i < tab.length;i++) {
            str += (i != (tab.length - 1)) ? tab[i] + "-" : tab[i];
        }
        map.get("setFloorStatu").put("rl_building", bat);
        map.get("setFloorStatu").put("rl_floor", etage);
        map.get("setFloorStatu").put("rl_floor_to_set", str);
        String responses = getSend("setFloorStatu");
    }

    public String[] getDispoEtage(String bat, String etage) {
        map.get("getFloorStatu").put("rl_building", bat);
        map.get("getFloorStatu").put("rl_floor", etage);
        String responses = getSend("getFloorStatu");
        String[] str = responses.split("-");
        return str;
    }

    public int getNbSalleDispo(String bat, String etage) {
        map.get("getNbFloorFree").put("rl_building", bat);
        map.get("getNbFloorFree").put("rl_floor", etage);
        String responses = getSend("getNbFloorFree");
        return Integer.parseInt(responses);
    }

    public String[] getDispoBat(String bat, String nbSalle) {
        map.get("getDispoBat").put("rl_building", bat);
        map.get("getDispoBat").put("rl_nb_loc", nbSalle);
        String responses = getSend("getDispoBat");
        return responses.split("-");
    }

    public String[] getDispoBatEvo(String bat, String nbPS, String nbB, String nbSO, String nbSC) {
        map.get("getDispoBatEvo").put("rl_building", bat);
        map.get("getDispoBatEvo").put("rl_nb_loc_ps", nbPS);
        map.get("getDispoBatEvo").put("rl_nb_loc_b", nbB);
        map.get("getDispoBatEvo").put("rl_nb_loc_so", nbSO);
        map.get("getDispoBatEvo").put("rl_nb_loc_sc", nbSC);
        String responses = getSend("getDispoBatEvo");
        return responses.split("-");
    }

    public void setStatuResa(String bat, String statu) {
        map.get("setStatuResa").put("rl_building", bat);
        map.get("setStatuResa").put("rl_resa", statu);
        String responses = getSend("setStatuResa");
    }

    public void setCompanyName(String name, String resp) {
        map.get("insertCompany").put("rl_company_name", name);
        map.get("insertCompany").put("rl_resp", resp);
        String responses = getSend("insertCompany");
    }
}
