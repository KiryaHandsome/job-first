import {apiCall} from "../utils/api.js";


export const applyToVacancy = (vacancyId) => {
    return apiCall('com.job.vacancy.apply', {
        vacancyId: vacancyId
    })
};

export const getEmployerVacancies = () => {
    return apiCall('com.job.vacancy.get_employer_vacancies')
};

