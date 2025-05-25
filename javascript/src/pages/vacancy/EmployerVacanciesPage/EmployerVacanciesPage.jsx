import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {apiCall} from '../../../utils/api.js';
import './EmployerVacanciesPage.css';
import {getEmployerVacancies} from "../../../services/VacancyService.js";
import {EMPLOYER_ROLE, experienceLevelsMap, workTypesMap} from "../../../constants/Common.jsx";
import Notification from "../../../components/common/Notification/Notification.jsx";
import {useAuth} from "../../../hooks/useAuth.jsx";
import {formatSalary} from "../../../utils/utils.js";

const EmployerVacanciesPage = () => {
    const [vacancies, setVacancies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const {getCurrentUser} = useAuth();
    const [notification, setNotification] = useState()

    const fetchVacancies = async () => {
        try {
            setLoading(true);
            const response = await getEmployerVacancies();

            console.log("EmployerVacancies: " + JSON.stringify(response))

            setVacancies(response);
        } catch (error) {
            setError('Не удалось загрузить вакансии');
            console.error('Error fetching vacancies:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const user = getCurrentUser();

        console.log("Emp.vac.page: " + JSON.stringify(user));

        if (user?.role !== EMPLOYER_ROLE) {
            console.log("Not employer. Redirect to all vacancies");
            navigate('/vacancies');
            return;
        }

        fetchVacancies();
    }, []);

    const handleCreateClick = () => {
        navigate('/create-vacancy');
    };

    const handleEditClick = (id) => {
        navigate(`/vacancies/${id}/edit`);
    };

    const handleDeleteClick = async (id) => {
        if (window.confirm('Вы уверены, что хотите удалить эту вакансию?')) {
            try {
                const response = await apiCall('http://localhost:8080/api/com.job.vacancy.delete_vacancy', {
                    id: id
                });
                if (!response.ok) {
                    throw new Error('Failed to delete vacancy');
                }
                setVacancies(vacancies.filter(v => v.id !== id));
            } catch (error) {
                setError('Не удалось удалить вакансию');
                console.error('Error deleting vacancy:', error);
            }
        }
    };

    if (loading) {
        return <div className="my-vacancies-page loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="my-vacancies-page error">{error}</div>;
    }

    return (
        <div className="my-vacancies-page">
            {notification && (
                <Notification
                    type={notification.type}
                    message={notification.message}
                    onClose={closeNotification}
                />
            )}
            <div className="my-vacancies-header">
                <h1>Мои вакансии</h1>
                <button className="create-vacancy-button" onClick={handleCreateClick}>
                    Создать вакансию
                </button>
            </div>

            {vacancies.length === 0 ? (
                <div className="no-vacancies">
                    <p>У вас пока нет созданных вакансий.</p>
                    <button className="create-vacancy-button" onClick={handleCreateClick}>
                        Создать первую вакансию
                    </button>
                </div>
            ) : (
                <div className="vacancies-grid">
                    {vacancies.map((vacancy) => (
                        <div
                            key={vacancy.id}
                            className="vacancy-card"
                            onClick={() => navigate(`/employer-vacancies/${vacancy.id}`)}
                            style={{cursor: 'pointer'}}
                        >
                            <div className="vacancy-header">
                                <h2 className="vacancy-title">{vacancy.title}</h2>
                                <div
                                    className="vacancy-salary">{formatSalary(vacancy.salaryMin, vacancy.salaryMax)}</div>
                            </div>

                            <div className="vacancy-info">
                                <div className="info-item">
                                    <span className="label">Локация:</span>
                                    <span className="value">{vacancy.location || 'Не указана'}</span>
                                </div>
                                <div className="info-item">
                                    <span className="label">Тип работы:</span>
                                    <span className="value">
                                        {workTypesMap[vacancy.workType]}
                                    </span>
                                </div>
                                <div className="info-item">
                                    <span className="label">Опыт:</span>
                                    <span className="value">
                                        {experienceLevelsMap[vacancy.experienceLevel]}
                                    </span>
                                </div>
                            </div>

                            {/*<div className="vacancy-actions">*/}
                            {/*    <button*/}
                            {/*        className="edit-button"*/}
                            {/*        onClick={() => handleEditClick(vacancy.id)}*/}
                            {/*    >*/}
                            {/*        Редактировать*/}
                            {/*    </button>*/}
                            {/*    <button*/}
                            {/*        className="delete-button"*/}
                            {/*        onClick={() => handleDeleteClick(vacancy.id)}*/}
                            {/*    >*/}
                            {/*        Удалить*/}
                            {/*    </button>*/}
                            {/*</div>*/}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default EmployerVacanciesPage;
