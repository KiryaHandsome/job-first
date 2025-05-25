import {apiCall} from "../utils/api.js";


export const applyToVacancy = (vacancyId, resumeId) => {
    console.log("vacancyId: " + vacancyId)
    console.log("resumeId: " + resumeId)
    return apiCall('com.job.vacancy.apply', {
        vacancyId: vacancyId,
        resumeId: resumeId,
    })
};

export const getEmployerVacancies = () => {
    return apiCall('com.job.vacancy.get_employer_vacancies')
};

export const createVacancy = (formData) => {
    return apiCall("com.job.vacancy.create", formData)
}

export const getEmployerVacancyDetails = (vacancyId) => {
    return apiCall("com.job.vacancy.get_employer_vacancy_details", {
        vacancyId: vacancyId
    })
}

export const editEmployerVacancy = (id, formData) => {
    formData["id"] = id
    return apiCall("com.job.vacancy.edit", formData)
}

export const editEmployerVacancyVisibility = (id, status) => {
    return apiCall("com.job.vacancy.edit_visibility", {
        id: id,
        status: status
    })
}