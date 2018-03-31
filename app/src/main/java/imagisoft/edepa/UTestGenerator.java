package imagisoft.edepa;

import java.util.ArrayList;

public class UTestGenerator {

    public static ArrayList<ScheduleEvent> createSchedule(){

        ArrayList<ScheduleEvent> schedule = new ArrayList<>();

        //#########################################################################################/

        schedule.add( new ScheduleEvent(

                "c1",
                "6/12/17 9:00 am",
                "6/12/17 11:50 am",
                "Auditorio del Centro de Artes",
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
                "6/12/17 9:00 am",
                "6/12/17 11:50 am",
                "Auditorio del Centro de Artes",
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
                "Auditorio del Centro de Artes",
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

        return schedule;

    }

}
