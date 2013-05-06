package model

/**
 *
 * parte truculenta porque el estado en pag del senado no corresponde con el del inegi
 * igual y despues hacemos una relacion en la BD por lo proto aqui va en forma de
 * Indice ingi -> valor senado
 *
 * Date: 5/5/13
 * @author Edgar Chan
 */
trait Estados {

  val listaEdos =
    Map(
      1  -> (2, "Aguas Calientes"),
      2	 -> (3, "Baja California" ),
      3	 -> (4, " Baja California Sur"),
      4	 -> (5, " Campeche"),
      7	 -> (6, "Chiapas"),
      8	 -> (7, "Chihuahua"),
      5	 -> (8, "Coahuila"),
      6	 -> (9, "Colima"),
      9	 -> (10, "Distrito Federal"),
      10	-> (11, "Durango"),
      12	-> (13, "Guerrero"),
      11	-> (12, "Guanajuato"),
      13	-> (14, "Hidalgo"),
      14	-> (15, "Jalisco"),
      15	-> (16, "México"),
      16	-> (17, "Michoacán"),
      17	-> (18, "Morelos"),
      18	-> (19, "Nayarit"),
      19	-> (20, "Nuevo León"),
      20	-> (21, "Oaxaca"),
      21	-> (22, "Puebla"),
      22	-> (23, "Querétaro"),
      23	-> (24, "Quintana Roo"),
      25	-> (26, "Sinaloa"),
      24	-> (25, "San Luis Potosí"),
      26	-> (27, "Sonora"),
      27	-> (28, "Tabasco"),
      28	-> (29, "Tamaulipas"),
      29	-> (30, "Tlaxcala"),
      30	-> (31, "Veracruz"),
      31	-> (32, "Yucatán"),
      32	-> (33, "Zacatecas")
    )

    def nombreEdo(inegiID:Int)={
      listaEdos.get(inegiID).map( _._2).getOrElse("ND")
    }

    def idSenado(inegiID:Int)={
      listaEdos.get(inegiID).map( _._1)
    }

}
