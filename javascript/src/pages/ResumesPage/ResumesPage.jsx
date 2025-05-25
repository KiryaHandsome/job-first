import {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {apiCall} from '../../utils/api';
import {CreateResumeModal} from '../../components/features/resumes/CreateResumeModal/CreateResumeModal';
import './ResumesPage.css';
import {getUserResumes} from "../../services/ResumeService.js";

export function ResumesPage() {
    const navigate = useNavigate();
    const [resumes, setResumes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    useEffect(() => {
        fetchResumes();
    }, []);

    const fetchResumes = async () => {
        try {
            setLoading(true);
            const response = await getUserResumes();
            console.log('API Response:', response);
            setResumes(response);
            setError(null);
        } catch (err) {
            console.error('Error fetching resumes:', err);
            setError('Не удалось загрузить резюме');
        } finally {
            setLoading(false);
        }
    };

    const handleCreateResume = async (resumeData) => {
        try {
            await apiCall('com.job.resume.create_resume', resumeData);
            setIsCreateModalOpen(false);
            fetchResumes();
        } catch (err) {
            console.error('Error creating resume:', err);
            setError('Не удалось создать резюме');
        }
    };

    const handleResumeClick = (resumeId) => {
        navigate(`/resumes/${resumeId}`);
    };

    if (loading) {
        return <div className="loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    return (
        <div className="resumes-page">
            <div className="resumes-header">
                <h1>Мои резюме</h1>
                <button
                    className="create-resume-button"
                    onClick={() => setIsCreateModalOpen(true)}
                >
                    Создать резюме
                </button>
            </div>

            {resumes.length === 0 ? (
                <div className="no-resumes">
                    <p>У вас пока нет резюме</p>
                    <button
                        className="create-resume-button"
                        onClick={() => setIsCreateModalOpen(true)}
                    >
                        Создать первое резюме
                    </button>
                </div>
            ) : (
                <div className="resumes-list">
                    {resumes.map(resume => (
                        <div
                            key={resume.id}
                            className="resume-card"
                            onClick={() => handleResumeClick(resume.id)}
                        >
                            <h3 className="resume-title">{resume.title}</h3>
                            <p className="resume-summary">{resume.summary}</p>
                            <p className="resume-status">
                                Статус: {resume.isActive ? 'Видно всем' : 'Не видно никому'}
                            </p>
                        </div>
                    ))}
                </div>
            )}

            <CreateResumeModal
                isOpen={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                onSubmit={handleCreateResume}
            />
        </div>
    );
}