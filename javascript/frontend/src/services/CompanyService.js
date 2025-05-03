import {apiCall} from "../utils/api.js";


export const getCompanyForEmployer = () => {
    return apiCall('com.job.company.get_company_for_employer')
};


export const createCompany = (companyInfo) => {
    return apiCall('com.job.company.create', companyInfo)
};

export const editCompany = (companyInfo) => {
    return apiCall('com.job.company.edit', companyInfo)
};



