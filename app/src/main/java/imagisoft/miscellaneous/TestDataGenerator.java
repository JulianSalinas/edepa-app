package imagisoft.miscellaneous;

import java.util.ArrayList;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.Message;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.ScheduleEventType;
import imagisoft.rommie.ChatFragment;

public class TestDataGenerator {

    public static ArrayList<Message> createNews(){

        ChatFragment helper = new ChatFragment();
        ArrayList<Message> news = new ArrayList<>();

        news.add(helper.createMessage(
                "Si quieres saber más de los creadores visita el " +
                        "siguiente enlace https://github.com/JulianSalinas/Rommie"));

        news.add(helper.createMessage(
                "Puedes conocer más del congreso en la siguiente página " +
                        "http://tecdigital.tec.ac.cr:8088/congresos/index.php/edepa/6_edepa"
        ));

        news.add(helper.createMessage(
                "Conoce más acerca de nuestros própositos en Youtube " +
                        "https://www.youtube.com/watch?v=Eccj5tb3nZs"
        ));

        return news;
    }

    public static ArrayList<ScheduleEvent> createSchedule(){

        ArrayList<ScheduleEvent> schedule = new ArrayList<>();

        //#########################################################################################/
        // Conferencias
        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c1",
                "6/12/17 9:00 am",
                "6/12/17 11:50 am",
                "Auditorio del Centro de las Artes",
                "Reflexiones sobre las gráficas en las clases de estadística",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Carolina Carvalho", "Portugal"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c2",
                "9/12/17 8:00 am",
                "9/12/17 8:50 am",
                "Auditorio D3",
                "La Profesión Misteriosa... La Estadística",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Sergio Hernández Gonzáles", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c3",
                "9/12/17 8:00 am",
                "9/12/17 8:50 am",
                "Auditorio A4-05",
                "Las actitudes hacia la estadística estudiantes mexicanos de bachillerato",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Jesús Salinas Herrera", "México"))
                .addExhibitor(new Exhibitor("Silvia Mayén Galicia", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c4",
                "5/12/17 9:00 am",
                "5/12/17 11:50 am",
                "Auditorio del Centro de Artes",
                "Trayectorias hipotéticas de aprendizaje y las ideas fundamentales de la Estadística",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Ernesto Alonso Sánchez Sánchez", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c5",
                "7/12/17 10:40 am",
                "7/12/17 11:50 am",
                "Auditorio del Centro de las Artes",
                "Las grandes ideas del pensamiento probabilístico y su enseñanza en un ambiente rico en tecnología",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Ernesto Alonso Sánchez Sánchez", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c7",
                "9/12/17 3:30 pm",
                "9/12/17 5:00 pm",
                "Auditorio D3",
                "Metodología para implementar la investigación reproducible en educación",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Jesús Humberto Cuevas Acosta", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c8",
                "8/12/17 8:00 am",
                "8/12/17 8:50 am",
                "Auditorio del Centro de las Artes",
                "Elija las tareas para las clases estadísticas ¿Una tarea fácil?",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Carolina Carvalho", "Portugal"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c9",
                "9/12/17 3:30 pm",
                "9/12/17 5:00 pm",
                "Auditorio A4-05",
                "Resolución de problemas en estadística según los programas del MEP",
                ScheduleEventType.CONFERENCIA)

                .addExhibitor(new Exhibitor("Edwin Chaves Esquivel", "Costa Rica"))

        );

        //#########################################################################################/
        // Talleres
        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "T13",
                "7/12/17 3:30 pm",
                "7/12/17 5:00 pm",
                "Aula B1-03",
                "Propuesta para la enseñanza de probabilidad condicional y eventos independientes mediante el uso de paradoj",
                ScheduleEventType.TALLER)

                .addExhibitor(new Exhibitor("Nazarelle Rojas Machado", "Costa Rica"))
                .addExhibitor(new Exhibitor("Carmen Hernández López", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "T14",
                "9/12/17 1:30 pm",
                "9/12/17 3:00 pm",
                "Aula B1-03",
                "Propuesta didáctica para enseñar principios de conteo en secundaria ",
                ScheduleEventType.TALLER)

                .addExhibitor(new Exhibitor("Johana Gómez", "Costa Rica"))
                .addExhibitor(new Exhibitor("Luis E. Ramírez", "Costa Rica"))
                .addExhibitor(new Exhibitor("Marisol Solano", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "T01",
                "7/12/17 1:30 pm",
                "7/12/17 5:00 pm",
                "Aula A4-05",
                "Resolución de problemas de estadística y probabilidad para primaria",
                ScheduleEventType.TALLER)

                .addExhibitor(new Exhibitor("Adriana Solís Arguedas", "Costa Rica"))
                .addExhibitor(new Exhibitor("Sandra Schmidt Quesada", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "T06",
                "9/12/17 1:30 pm",
                "9/12/17 3:00 pm",
                "Aula A4-05",
                "La historia de la ciencia como recurso didáctico para la enseñanza de la probabilidad y la estadística",
                ScheduleEventType.TALLER)

                .addExhibitor(new Exhibitor("Jesús Salinas Herrera", "México"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "T11",
                "7/12/17 1:30 pm",
                "7/12/17 5:00 pm",
                "Biblioteca",
                "Resolución de problemas en estadística según los programas del MEP",
                ScheduleEventType.TALLER)

                .addExhibitor(new Exhibitor("Luis E. Carrera", "Costa Rica"))
                .addExhibitor(new Exhibitor("Jessica Navarro", "Costa Rica"))
                .addExhibitor(new Exhibitor("Reiman Acuña C.", "Costa Rica"))

        );

        //#########################################################################################/
        // Ponencias
        //#########################################################################################/


        schedule.add( new ScheduleEvent(

                "P01",
                "8/12/17 9:40 am",
                "8/12/17 10:10 am",
                "Auditorio D3",
                "El azar: el gran invento del ser humano",
                ScheduleEventType.PONENCIA)

                .addExhibitor(new Exhibitor("Diego Solís", "Costa Rica"))
                .addExhibitor(new Exhibitor("Manuel Chavez", "Costa Rica"))
                .addExhibitor(new Exhibitor("Jerson Valverde", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "P02",
                "8/12/17 9:00 am",
                "8/12/17 9:30 am",
                "Auditorio D3",
                "La probabilidad como elemento orientador de la toma de decisiones",
                ScheduleEventType.PONENCIA)

                .addExhibitor(new Exhibitor("Giovanni Sanabria B.", "Costa Rica"))
                .addExhibitor(new Exhibitor("Félix Núñez V.", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "P03",
                "9/12/17 9:40 am",
                "9/12/17 10:10 am",
                "Aula B1-06",
                "Relación entre estereotipos de género y el rendimiento en matemáticas: Un estudio comparativo entre jóvenes de secundaria y universitarias",
                ScheduleEventType.PONENCIA)

                .addExhibitor(new Exhibitor("Tannia Moreira", "Costa Rica"))
                .addExhibitor(new Exhibitor("Vanessa Smith", "Costa Rica"))
                .addExhibitor(new Exhibitor("Eiliana Montero", "Costa Rica"))
                .addExhibitor(new Exhibitor("José Zamora", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "P04",
                "9/12/17 9:00 am",
                "9/12/17 9:30 am",
                "Auditorio D3",
                "La Estadística y la Probabilidad en el Deporte",
                ScheduleEventType.PONENCIA)

                .addExhibitor(new Exhibitor("Randall Alberto Brenes Gómez", "Costa Rica"))

        );

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "P05",
                "8/12/17 9:00 am",
                "8/12/17 9:30 am",
                "Aula A4-05",
                "Investigaciones sobre gráficos estadísticos en Educación Primaria: revisión de la literatura",
                ScheduleEventType.PONENCIA)

                .addExhibitor(new Exhibitor("Danilo Díaz-Levicoy", "España"))
                .addExhibitor(new Exhibitor("Pedro Arteaga", "España"))
                .addExhibitor(new Exhibitor("Carmen Batanero", "Costa Rica"))

        );

        //#########################################################################################/

        return schedule;

    }

}
