import {apiCall} from "../utils/api.js";


export const getUserResumes = () => {
    return apiCall('com.job.resume.get_user_resumes')
};
